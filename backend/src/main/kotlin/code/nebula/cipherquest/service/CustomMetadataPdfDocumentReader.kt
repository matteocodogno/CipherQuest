package code.nebula.cipherquest.service

import org.apache.pdfbox.pdfparser.PDFParser
import org.apache.pdfbox.pdmodel.PDDocument
import org.slf4j.LoggerFactory
import org.springframework.ai.document.Document
import org.springframework.ai.document.DocumentReader
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig
import org.springframework.ai.reader.pdf.layout.PDFLayoutTextStripperByArea
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.Resource
import org.springframework.util.CollectionUtils
import org.springframework.util.StringUtils
import java.awt.Rectangle

/**
 * Groups the parsed PDF pages into [Document]s. You can group one or more pages
 * into a single output document. Use [PdfDocumentReaderConfig] for customization
 * options. The default configuration is: - pagesPerDocument = 1 - pageTopMargin = 0 -
 * pageBottomMargin = 0
 */
class CustomMetadataPdfDocumentReader : DocumentReader {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val document: PDDocument
    private var config: PdfDocumentReaderConfig
    private var resourceFileName: String?
    var customMetadata: MutableMap<String, Any>

    companion object {
        private const val PDF_PAGE_REGION = "pdfPageRegion"
        const val METADATA_START_PAGE_NUMBER = "page_number"
        const val METADATA_END_PAGE_NUMBER = "end_page_number"
        const val METADATA_FILE_NAME = "file_name"
        const val MAX_PAGE_NUMBER_FOR_HIGH_LOG_FREQUENCY = 10
    }

    constructor(resourceUrl: String) : this(DefaultResourceLoader().getResource(resourceUrl))

    constructor(pdfResource: Resource) : this(pdfResource, PdfDocumentReaderConfig.defaultConfig())

    constructor(
        resourceUrl: String,
        config: PdfDocumentReaderConfig,
    ) : this(DefaultResourceLoader().getResource(resourceUrl), config)

    constructor(pdfResource: Resource, config: PdfDocumentReaderConfig) {
        val pdfParser =
            PDFParser(
                org.apache.pdfbox.io
                    .RandomAccessReadBuffer(pdfResource.inputStream),
            )
        this.document = pdfParser.parse()
        this.resourceFileName = pdfResource.filename
        this.config = config
        this.customMetadata = mutableMapOf()
    }

    override fun get(): List<Document> {
        val readDocuments = mutableListOf<Document>()
        val pdfTextStripper = PDFLayoutTextStripperByArea()

        var pageNumber = 0
        var pagesPerDocument = 0
        var startPageNumber = 0

        val pageTextGroupList = mutableListOf<String>()

        val totalPages = this.document.documentCatalog.pages.count
        // if less than 10 pages, print each iteration
        val logFrequency =
            if (
                totalPages > MAX_PAGE_NUMBER_FOR_HIGH_LOG_FREQUENCY
            ) {
                totalPages / MAX_PAGE_NUMBER_FOR_HIGH_LOG_FREQUENCY
            } else {
                1
            }

        for ((counter, page) in this.document.documentCatalog.pages
            .withIndex()) {
            if (counter % logFrequency == 0 && counter / logFrequency < MAX_PAGE_NUMBER_FOR_HIGH_LOG_FREQUENCY) {
                logger.info("Processing PDF page: {}", (counter + 1))
            }

            pagesPerDocument++

            if (
                this.config.pagesPerDocument != PdfDocumentReaderConfig.ALL_PAGES &&
                pagesPerDocument >= this.config.pagesPerDocument
            ) {
                pagesPerDocument = 0

                val aggregatedPageTextGroup = pageTextGroupList.joinToString("")
                if (StringUtils.hasText(aggregatedPageTextGroup)) {
                    readDocuments.add(toDocument(aggregatedPageTextGroup, startPageNumber, pageNumber))
                }
                pageTextGroupList.clear()

                startPageNumber = pageNumber + 1
            }
            val x0 = page.mediaBox.lowerLeftX.toInt()
            val xW = page.mediaBox.width.toInt()

            val y0 = page.mediaBox.lowerLeftY.toInt() + this.config.pageTopMargin
            val yW = page.mediaBox.height.toInt() - (this.config.pageTopMargin + this.config.pageBottomMargin)

            pdfTextStripper.addRegion(PDF_PAGE_REGION, Rectangle(x0, y0, xW, yW))
            pdfTextStripper.extractRegions(page)
            var pageText = pdfTextStripper.getTextForRegion(PDF_PAGE_REGION)

            if (StringUtils.hasText(pageText)) {
                pageText = this.config.pageExtractedTextFormatter.format(pageText, pageNumber)
                pageTextGroupList.add(pageText)
            }
            pageNumber++
            pdfTextStripper.removeRegion(PDF_PAGE_REGION)
        }
        if (!CollectionUtils.isEmpty(pageTextGroupList)) {
            readDocuments.add(toDocument(pageTextGroupList.joinToString(""), startPageNumber, pageNumber))
        }
        logger.info("Processing {} pages", totalPages)
        return readDocuments
    }

    private fun toDocument(
        docText: String,
        startPageNumber: Int,
        endPageNumber: Int,
    ): Document {
        val doc = Document(docText)
        doc.metadata[METADATA_START_PAGE_NUMBER] = startPageNumber
        if (startPageNumber != endPageNumber) {
            doc.metadata[METADATA_END_PAGE_NUMBER] = endPageNumber
        }
        doc.metadata[METADATA_FILE_NAME] = this.resourceFileName
        doc.metadata.putAll(customMetadata)

        return doc
    }
}
