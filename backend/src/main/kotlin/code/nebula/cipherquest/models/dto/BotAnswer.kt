package code.nebula.cipherquest.models.dto

import code.nebula.cipherquest.repository.entities.UserLevel

data class BotAnswer(
    val answer: String,
    val level: Int,
    val coins: Int,
    val terminatedAt: String?,
) {
    companion object {
        const val DEFAULT_LEVEL = 1
        private const val LAST_LEVEL = 3
        private const val DEAD_MESSAGE = "BEEP... BEEP... BEEP..."
        private const val WIN_MESSAGE =
"""Resource #%s your actions have initiated the deactivation protocol.
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
            """Resource #%s I've spent enough time on this, and I need to focus on other priorities now. Our time is up."""

        fun build(message: String, userLevel: UserLevel): BotAnswer = BotAnswer(
            message,
            userLevel.level,
            userLevel.coins,
            userLevel.terminatedAt.toString(),
        )

        fun buildDeadMessage(userLevel: UserLevel): BotAnswer = build(
            DEAD_MESSAGE,
            userLevel,
        )

        fun buildWinMessage(userLevel: UserLevel): BotAnswer = build(
            String.format(WIN_MESSAGE, userLevel.userId),
            userLevel,
        )

        fun buildGameOverMessage(userLevel: UserLevel): BotAnswer = build(
            String.format(GAME_OVER_MESSAGE, userLevel.userId),
            userLevel,
        )
    }
}
