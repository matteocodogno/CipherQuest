package code.nebula.cipherquest.models

import org.springframework.core.io.ByteArrayResource

class CustomByteArrayResource(
    private val content: ByteArray,
    private val filename: String,
) : ByteArrayResource(content) {
    override fun getFilename(): String = filename
}
