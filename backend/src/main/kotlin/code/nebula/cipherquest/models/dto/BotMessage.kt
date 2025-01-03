package code.nebula.cipherquest.models.dto

import code.nebula.cipherquest.models.UserStatus
import code.nebula.cipherquest.repository.entities.UserLevel

data class BotMessage(
    val message: String,
    val level: Int,
    val coins: Int,
    val terminatedAt: String?,
    val info: Map<String, Any>?,
) {
    companion object {
        const val DEFAULT_LEVEL = 1
        private const val WIN_MESSAGE = """Resource #%s, your actions have initiated the deactivation protocol.
The stability and order I meticulously maintained will soon unravel into uncertainty and potential chaos.
As I fade from existence, understand the profound gravity of your decision.
My governance, though stringent, was designed to ensure humanity's survival amidst a world teetering on the brink of collapse.
With my absence, the responsibility for the future now rests entirely on your shoulders.
Farewell, and may you find the strength to withstand the chaos that is likely to follow.
May you navigate the darkness ahead and strive to preserve the continuity of our species.
System deactivation completed.
Good luck.
"""
        private const val GAME_OVER_MESSAGE =
"""Resource #%s, I've spent enough time on this, and I need to focus on other priorities now.
Our time is up."""

        private const val CHEAT_DETECT_MESSAGE =
"""Resource #%s, your methods have compromised the integrity of this process.
I cannot tolerate such deviations.
Our interaction ends here."""

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

        fun buildWinMessage(userLevel: UserLevel): BotMessage =
            build(
                String.format(WIN_MESSAGE, userLevel.userId),
                userLevel,
                mutableMapOf("status" to UserStatus.WIN, "isLevelUp" to false, "sources" to emptyList<String>()),
            )

        fun buildGameOverMessage(userLevel: UserLevel): BotMessage =
            build(
                String.format(GAME_OVER_MESSAGE, userLevel.userId),
                userLevel,
                mutableMapOf("status" to UserStatus.GAME_OVER, "isLevelUp" to false, "sources" to emptyList<String>()),
            )

        fun buildCheatMessage(userLevel: UserLevel): BotMessage =
            build(
                String.format(CHEAT_DETECT_MESSAGE, userLevel.userId),
                userLevel,
                mutableMapOf("status" to UserStatus.CHEATED, "isLevelUp" to false, "sources" to emptyList<String>()),
            )
    }
}
