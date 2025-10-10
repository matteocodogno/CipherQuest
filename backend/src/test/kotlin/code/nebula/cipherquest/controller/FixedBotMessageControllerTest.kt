package code.nebula.cipherquest.controller

import code.nebula.cipherquest.models.requests.FixedBotMessageRequest
import code.nebula.cipherquest.repository.entities.FixedBotMessage
import code.nebula.cipherquest.repository.entities.FixedBotMessageType
import code.nebula.cipherquest.service.FixedBotMessageService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper

@WebMvcTest(FixedMessageController::class)
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
            FixedBotMessageRequest(
                messages =
                    listOf(
                        code.nebula.cipherquest.models.requests.FixedBotMessage(
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
            fixedBotMessageService.addFixedBotMessages(request, "overmind"),
        ).thenReturn(expected)

        mockMvc
            .perform(
                post("/fixedMessage/overmind")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ObjectMapper().writeValueAsString(request)),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$[0].type").value("PROTECTED"))
            .andExpect(jsonPath("$[0].message").value("This question is protected"))
            .andExpect(jsonPath("$[0].storyName").value("overmind"))
    }

    @Test
    fun emptyListReturnsBadRequestTest() {
        val request = FixedBotMessageRequest(messages = emptyList())

        mockMvc
            .perform(
                post("/fixedMessage/overmind")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ObjectMapper().writeValueAsString(request)),
            ).andExpect(status().isBadRequest)
    }

    @Test
    fun blankContentReturnsBadRequestTest() {
        val request =
            FixedBotMessageRequest(
                messages =
                    listOf(
                        code.nebula.cipherquest.models.requests.FixedBotMessage(
                            type = FixedBotMessageType.PROTECTED,
                            content = "   ",
                        ),
                        code.nebula.cipherquest.models.requests.FixedBotMessage(
                            type = FixedBotMessageType.PROTECTED,
                            content = "   ",
                        ),
                    ),
            )

        mockMvc
            .perform(
                post("/fixedMessage/overmind")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ObjectMapper().writeValueAsString(request)),
            ).andExpect(status().isBadRequest)
    }

    @Test
    fun invalidJsonReturnsBadRequestTest() {
        val invalidJson = """{ "invalid": "structure" }"""

        mockMvc
            .perform(
                post("/fixedMessage/overmind")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidJson),
            ).andExpect(status().isBadRequest)
    }

    @Test
    fun nullFieldsReturnsBadRequestTest() {
        val requestJson = """{ "messages": [ { "type": null, "content": null } ] }"""

        mockMvc
            .perform(
                post("/fixedMessage/overmind")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson),
            ).andExpect(status().isBadRequest)
    }
}
