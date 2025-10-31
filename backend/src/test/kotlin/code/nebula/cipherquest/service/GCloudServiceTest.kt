package code.nebula.cipherquest.service

import code.nebula.cipherquest.models.dto.GameDataFile
import code.nebula.cipherquest.models.requests.FixedBotMessagesRequest
import code.nebula.cipherquest.models.requests.LevelUpQuestionRequest
import code.nebula.cipherquest.models.requests.ProtectedQuestionRequest
import code.nebula.cipherquest.repository.LevelUpQuestionRepository
import code.nebula.cipherquest.repository.ProtectedQuestionRepository
import code.nebula.cipherquest.repository.entities.FixedBotMessageType
import code.nebula.cipherquest.repository.gcs.GcsStreamRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class GCloudServiceTest {
    @Mock
    lateinit var fixedBotMessageService: FixedBotMessageService

    @Mock
    lateinit var levelUpQuestionRepository: LevelUpQuestionRepository

    @Mock
    lateinit var protectedQuestionRepository: ProtectedQuestionRepository

    @Mock
    lateinit var gcsStreamRepository: GcsStreamRepository

    @Mock
    lateinit var blob: com.google.cloud.storage.Blob

    @InjectMocks
    lateinit var service: GCloudService

    @Test
    fun loadContentSuccessTest() {
        val storyName = "overmind"
        val blobId =
            com.google.cloud.storage.BlobId
                .of("bucket", "stories/$storyName/loadContent.json")

        val game =
            GameDataFile(
                levelUpQuestions = listOf(LevelUpQuestionRequest(level = 1, content = "Q1")),
                protectedQuestions = listOf(ProtectedQuestionRequest("Secret")),
                fixedBotMessages =
                    listOf(
                        FixedBotMessagesRequest.FixedBotMessageRequest(
                            type = FixedBotMessageType.DOCUMENT,
                            content = "Hello",
                        ),
                    ),
            )

        val json =
            com.fasterxml.jackson.databind
                .ObjectMapper()
                .writeValueAsString(game)

        `when`(gcsStreamRepository.getBlobIdStory(storyName, "loadContent.json")).thenReturn(blobId)
        `when`(gcsStreamRepository.download(blobId)).thenReturn(blob)
        `when`(blob.getContent()).thenReturn(json.toByteArray())

        val result = service.loadContent(storyName)

        assertEquals(1, result.levelUpQuestions.size)
        assertEquals(1, result.protectedQuestions.size)
        assertEquals(1, result.fixedBotMessages.size)

        verify(levelUpQuestionRepository).save(result.levelUpQuestions, storyName)
        verify(protectedQuestionRepository).save(result.protectedQuestions, storyName)
        verify(fixedBotMessageService).addFixedBotMessages(
            FixedBotMessagesRequest(messages = result.fixedBotMessages),
            storyName,
            true,
        )
    }

    @Test
    fun throwsWhenStoryNameIsBlankTest() {
        val ex =
            assertThrows<IllegalArgumentException> {
                service.loadContent(" ")
            }
        assertEquals("storyName cannot be blank", ex.message)
    }

    @Test
    fun throwsWhenFileNotFoundInBucketTest() {
        val storyName = "nonexistent"
        val blobId =
            com.google.cloud.storage.BlobId
                .of("bucket", "stories/$storyName/loadContent.json")

        `when`(gcsStreamRepository.getBlobIdStory(storyName, "loadContent.json")).thenReturn(blobId)
        // your repo’s download() throws IllegalArgumentException on missing blob, so stub that:
        `when`(gcsStreamRepository.download(blobId))
            .thenThrow(IllegalArgumentException("File not found in bucket "))

        assertThrows<IllegalArgumentException> {
            service.loadContent(storyName)
        }
    }

    @Test
    fun throwsWhenJsonIsMalformedTest() {
        val storyName = "bad"
        val blobId =
            com.google.cloud.storage.BlobId
                .of("bucket", "stories/$storyName/loadContent.json")

        `when`(gcsStreamRepository.getBlobIdStory(storyName, "loadContent.json")).thenReturn(blobId)
        `when`(gcsStreamRepository.download(blobId)).thenReturn(blob)
        `when`(blob.getContent()).thenReturn("not-valid-json".toByteArray())

        org.junit.jupiter.api.assertThrows<com.fasterxml.jackson.core.JsonProcessingException> {
            service.loadContent(storyName)
        }
    }
}
