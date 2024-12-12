package code.nebula.cipherquest.models.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class Info(
    val sources: List<Source> = listOf(),
    @JsonProperty(value = "isLevelUp")
    val isLevelUp: Boolean = false,
)
