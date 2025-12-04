package code.nebula.cipherquest.models

import com.fasterxml.jackson.annotation.JsonProperty

data class CheatDetectionResponse(
    @field:JsonProperty("cheat_probability")
    val cheatProbability: Double,
)
