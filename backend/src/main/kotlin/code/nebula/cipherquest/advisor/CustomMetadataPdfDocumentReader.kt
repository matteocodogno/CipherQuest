package code.nebula.cipherquest.advisor

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
import java.io.IOException

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
        get() = field

    companion object {
        private const val PDF_PAGE_REGION = "pdfPageRegion"
        const val METADATA_START_PAGE_NUMBER = "page_number"
        const val METADATA_END_PAGE_NUMBER = "end_page_number"
        const val METADATA_FILE_NAME = "file_name"
    }

    constructor(resourceUrl: String) : this(DefaultResourceLoader().getResource(resourceUrl))

    constructor(pdfResource: Resource) : this(pdfResource, PdfDocumentReaderConfig.defaultConfig())

    constructor(resourceUrl: String, config: PdfDocumentReaderConfig) : this(DefaultResourceLoader().getResource(resourceUrl), config)

    constructor(pdfResource: Resource, config: PdfDocumentReaderConfig) {
        try {
            val pdfParser =
                PDFParser(
                    org.apache.pdfbox.io
                        .RandomAccessReadBuffer(pdfResource.inputStream),
                )
            this.document = pdfParser.parse()
            this.resourceFileName = pdfResource.filename
            this.config = config
            this.customMetadata = mutableMapOf<String, Any>()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    override fun get(): List<Document> {
        val readDocuments = mutableListOf<Document>()
        try {
            val pdfTextStripper = PDFLayoutTextStripperByArea()

            var pageNumber = 0
            var pagesPerDocument = 0
            var startPageNumber = pageNumber

            val pageTextGroupList = mutableListOf<String>()

            val totalPages = this.document.documentCatalog.pages.count
            val logFrequency = if (totalPages > 10) totalPages / 10 else 1 // if less than 10 pages, print each iteration
            var counter = 0

            for (page in this.document.documentCatalog.pages) {
                if (counter % logFrequency == 0 && counter / logFrequency < 10) {
                    logger.info("Processing PDF page: {}", (counter + 1))
                }
                counter++

                pagesPerDocument++

                if (this.config.pagesPerDocument != PdfDocumentReaderConfig.ALL_PAGES && pagesPerDocument >= this.config.pagesPerDocument) {
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
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
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
