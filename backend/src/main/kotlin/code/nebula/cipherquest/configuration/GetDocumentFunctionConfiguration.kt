package code.nebula.cipherquest.configuration

// @Configuration
// class GetDocumentFunctionConfiguration {
//    @Bean
//    @Description("get document by name")
//    fun getDocument(vectorStoreService: VectorStoreService): java.util.function.Function<Request, String?> =
//        java.util.function.Function { req ->
//            req.let { (filename, level) ->
//                val document = vectorStoreService.getDocumentByFilename(filename, level)
//                document ?: throw NullPointerException("Document not found for filename: $filename and level: $level")
//            }
//        }
// }
//
// data class Request(
//    val filename: String,
//    val level: Int,
// ) {
//    constructor() : this(filename = "", level = 1)
// }
