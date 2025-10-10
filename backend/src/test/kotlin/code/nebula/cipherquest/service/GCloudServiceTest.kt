package code.nebula.cipherquest.service

import code.nebula.cipherquest.configuration.properties.CloudStorageProperties
import code.nebula.cipherquest.models.dto.GameDataFile
import code.nebula.cipherquest.models.requests.FixedBotMessage
import code.nebula.cipherquest.models.requests.LevelUpQuestionRequest
import code.nebula.cipherquest.models.requests.Prize
import code.nebula.cipherquest.models.requests.ProtectedQuestionRequest
import code.nebula.cipherquest.repository.LevelUpQuestionRepository
import code.nebula.cipherquest.repository.ProtectedQuestionRepository
import code.nebula.cipherquest.repository.entities.FixedBotMessageType
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.cloud.storage.Blob
import com.google.cloud.storage.Storage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class GCloudServiceTest {
    private val storage = mock(Storage::class.java)
    private val cloudStorageProperties = mock(CloudStorageProperties::class.java)
    private val prizeService = mock(PrizeService::class.java)
    private val fixedBotMessageService = mock(FixedBotMessageService::class.java)
    private val levelUpQuestionRepository = mock(LevelUpQuestionRepository::class.java)
    private val protectedQuestionRepository = mock(ProtectedQuestionRepository::class.java)
    private val bucketProperties = mock(CloudStorageProperties.BucketProperties::class.java)

    private val service =
        GCloudService(
            storage,
            cloudStorageProperties,
            prizeService,
            fixedBotMessageService,
            levelUpQuestionRepository,
            protectedQuestionRepository,
        )

    init {
        `when`(cloudStorageProperties.storiesBucket).thenReturn(bucketProperties)
    }

    @Test
    fun loadContentSuccessTest() {
        // Arrange
        val bucketName = "test-bucket"
        val folder = "stories"
        val storyName = "overmind"

        val mockGameData =
            GameDataFile(
                levelUpQuestions = listOf(LevelUpQuestionRequest(level = 1, content = "Q1")),
                protectedQuestions = listOf(ProtectedQuestionRequest("Secret")),
                fixedBotMessages = listOf(FixedBotMessage(type = FixedBotMessageType.DOCUMENT, content = "Hello")),
                prizes = listOf(Prize(name = "Gold", position = 1)),
            )

        val mockBlob = mock(Blob::class.java)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockGameData)

        `when`(cloudStorageProperties.storiesBucket).thenReturn(bucketProperties)
        `when`(bucketProperties.name).thenReturn(bucketName)
        `when`(bucketProperties.folder).thenReturn(folder)
        `when`(storage[bucketName, "$folder/$storyName/loadContent.json"]).thenReturn(mockBlob)
        `when`(mockBlob.getContent()).thenReturn(json.toByteArray())

        val result = service.loadContent(storyName)

        assertEquals(mockGameData.levelUpQuestions.size, result.levelUpQuestions.size)
        assertEquals(mockGameData.protectedQuestions.size, result.protectedQuestions.size)
        assertEquals(mockGameData.fixedBotMessages.size, result.fixedBotMessages.size)
        assertEquals(mockGameData.prizes.size, result.prizes.size)
    }

    @Test
    fun throwsWhenStoryNameIsBlankTest() {
        val exception =
            assertThrows<IllegalArgumentException> {
                service.loadContent(" ")
            }
        assertEquals("storyName cannot be blank", exception.message)
    }

    @Test
    fun throwsWhenFileNotFoundInBucketTest() {
        val storyName = "nonexistent"
        `when`(cloudStorageProperties.storiesBucket.name).thenReturn("bucket")
        `when`(cloudStorageProperties.storiesBucket.folder).thenReturn("folder")
        `when`(storage["bucket", "folder/$storyName/loadContent.json"]).thenReturn(null)

        val exception =
            assertThrows<IllegalArgumentException> {
                service.loadContent(storyName)
            }
        assertTrue(exception.message!!.contains("File not found in bucket"))
    }

    @Test
    fun throwsWhenJsonIsMalformedTest() {
        val storyName = "bad"
        val mockBlob = mock(Blob::class.java)

        `when`(cloudStorageProperties.storiesBucket.name).thenReturn("bucket")
        `when`(cloudStorageProperties.storiesBucket.folder).thenReturn("folder")
        `when`(storage["bucket", "folder/$storyName/loadContent.json"]).thenReturn(mockBlob)
        `when`(mockBlob.getContent()).thenReturn("not-valid-json".toByteArray())

        assertThrows<com.fasterxml.jackson.core.JsonProcessingException> {
            service.loadContent(storyName)
        }
    }
}
