package code.nebula.cipherquest.controller

import code.nebula.cipherquest.models.dto.GameDataFile
import code.nebula.cipherquest.service.GCloudService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.server.ResponseStatusException

@WebMvcTest(GCloudController::class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GCloudControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var gCloudService: GCloudService

    @Test
    fun successUponValidFileTest() {
        val mockData =
            GameDataFile(
                levelUpQuestions = emptyList(),
                protectedQuestions = emptyList(),
                fixedBotMessages = emptyList(),
            )

        `when`(gCloudService.loadContent("invalid")).thenReturn(mockData)

        mockMvc
            .perform(post("/gcloud/loadContent/invalid"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.levelUpQuestions").isArray)
            .andExpect(jsonPath("$.protectedQuestions").isArray)
            .andExpect(jsonPath("$.fixedBotMessages").isArray)

        verify(gCloudService).loadContent("invalid")
    }

    @Test
    fun failUponInvalidStoryNameTest() {
        `when`(gCloudService.loadContent("invalid"))
            .thenThrow(ResponseStatusException(HttpStatus.NOT_FOUND, "Story not found"))
        mockMvc
            .perform(post("/gcloud/loadContent/invalid"))
            .andExpect(status().isNotFound)
    }
}
