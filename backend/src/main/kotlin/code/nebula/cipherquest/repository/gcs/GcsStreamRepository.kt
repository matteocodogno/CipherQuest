package code.nebula.cipherquest.repository.gcs

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.cloud.storage.Blob
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import org.springframework.scheduling.annotation.Async
import java.io.OutputStream
import java.nio.channels.Channels

open class GcsStreamRepository(
    open val objectMapper: ObjectMapper,
    open val storage: Storage,
) {
    @Async
    open fun save(
        blobId: BlobId,
        writer: (OutputStream) -> Unit,
    ) {
        val blobInfo = BlobInfo.newBuilder(blobId).build()
        storage
            .create(blobInfo)
            .writer()
            .let(Channels::newOutputStream)
            .use(writer)
    }

    fun download(blobId: BlobId): Blob = storage.get(blobId)
}
