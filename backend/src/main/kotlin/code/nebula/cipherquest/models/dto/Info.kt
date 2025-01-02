package code.nebula.cipherquest.models.dto

import code.nebula.cipherquest.models.UserStatus
import com.fasterxml.jackson.annotation.JsonProperty

data class Info(
    val sources: List<Source> = listOf(),
    @JsonProperty(value = "isLevelUp")
    val isLevelUp: Boolean = false,
    val status: UserStatus = UserStatus.IN_PROGRESS,
)
