package code.nebula.cipherquest.models.dto

import code.nebula.cipherquest.repository.entities.UserLevel

data class BotMessage(
    val message: String,
    val level: Int,
    val coins: Int,
    val terminatedAt: String?,
    val info: Map<String, Any>?,
) {
    companion object {
        fun build(
            message: String,
            userLevel: UserLevel,
            map: MutableMap<String, Any>?,
        ): BotMessage =
            BotMessage(
                message,
                userLevel.level,
                userLevel.coins,
                userLevel.terminatedAt.toString(),
                map,
            )
    }
}
