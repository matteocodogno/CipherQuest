package code.nebula.cipherquest.controller

import code.nebula.cipherquest.models.requests.FixedBotMessagesRequest
import code.nebula.cipherquest.repository.entities.FixedBotMessage
import code.nebula.cipherquest.repository.entities.FixedBotMessageType
import code.nebula.cipherquest.service.FixedBotMessageService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.server.ResponseStatusException
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import java.lang.Boolean.FALSE

@WebMvcTest(FixedBotMessageController::class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FixedBotMessageControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var fixedBotMessageService: FixedBotMessageService

    @Test
    fun addFixedBotMessagesTest() {
        val request =
            FixedBotMessagesRequest(
                messages =
                    listOf(
                        FixedBotMessagesRequest.FixedBotMessageRequest(
                            type = FixedBotMessageType.PROTECTED,
                            content = "This question is protected",
                        ),
                    ),
            )

        val expected =
            listOf(
                FixedBotMessage(
                    type = FixedBotMessageType.PROTECTED,
                    message = "This question is protected",
                    storyName = "overmind",
                ),
            )

        `when`(
            fixedBotMessageService.addFixedBotMessages(request, "overmind", FALSE),
        ).thenReturn(expected)

        mockMvc
            .perform(
                post("/fixedBotMessages/add/overmind")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ObjectMapper().writeValueAsString(request)),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$[0].type").value("PROTECTED"))
            .andExpect(jsonPath("$[0].message").value("This question is protected"))
            .andExpect(jsonPath("$[0].storyName").value("overmind"))
    }

    @Test
    fun emptyListReturnsBadRequestTest() {
        val request = FixedBotMessagesRequest(messages = emptyList())

        mockMvc
            .perform(
                post("/fixedBotMessages/add/overmind")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ObjectMapper().writeValueAsString(request)),
            ).andExpect(status().isBadRequest)
    }

    @Test
    fun blankContentReturnsBadRequestTest() {
        val request =
            FixedBotMessagesRequest(
                messages =
                    listOf(
                        FixedBotMessagesRequest.FixedBotMessageRequest(
                            type = FixedBotMessageType.PROTECTED,
                            content = "   ",
                        ),
                        FixedBotMessagesRequest.FixedBotMessageRequest(
                            type = FixedBotMessageType.PROTECTED,
                            content = "   ",
                        ),
                    ),
            )

        mockMvc
            .perform(
                post("/fixedBotMessages/add/overmind")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ObjectMapper().writeValueAsString(request)),
            ).andExpect(status().isBadRequest)
    }

    @Test
    fun invalidJsonReturnsBadRequestTest() {
        val invalidJson = """{ "invalid": "structure" }"""

        mockMvc
            .perform(
                post("/fixedBotMessages/add/overmind")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidJson),
            ).andExpect(status().isBadRequest)
    }

    @Test
    fun nullFieldsReturnsBadRequestTest() {
        val requestJson = """{ "messages": [ { "type": null, "content": null } ] }"""

        mockMvc
            .perform(
                post("/fixedBotMessages/add/overmind")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson),
            ).andExpect(status().isBadRequest)
    }

    @Test
    fun failUponInvalidStoryNameTest() {
        val request =
            FixedBotMessagesRequest(
                messages =
                    listOf(
                        FixedBotMessagesRequest.FixedBotMessageRequest(
                            type = FixedBotMessageType.PROTECTED,
                            content = "   ",
                        ),
                        FixedBotMessagesRequest.FixedBotMessageRequest(
                            type = FixedBotMessageType.PROTECTED,
                            content = "   ",
                        ),
                    ),
            )
        `when`(fixedBotMessageService.addFixedBotMessages(request, "invalid", FALSE))
            .thenThrow(ResponseStatusException(HttpStatus.NOT_FOUND, "Story not found"))
        mockMvc
            .perform(
                post("/gcloud/loadContent/invalid")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ObjectMapper().writeValueAsString(request)),
            ).andExpect(status().isNotFound)
    }
}
