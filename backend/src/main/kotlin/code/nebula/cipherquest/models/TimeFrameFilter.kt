package code.nebula.cipherquest.models

import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

enum class TimeFrameFilter {
    TODAY,
    LAST_WEEK,
    LAST_MONTH,
    LAST_YEAR,
    ALL,
    ;

    companion object {
        const val TEN_YEARS = 10L
    }

    fun startDate(): OffsetDateTime =
        OffsetDateTime
            .now()
            .truncatedTo(ChronoUnit.DAYS)
            .let { offsetDateTime ->
                when (this@TimeFrameFilter) {
                    TODAY -> offsetDateTime
                    LAST_WEEK -> offsetDateTime.minusWeeks(1)
                    LAST_MONTH -> offsetDateTime.minusMonths(1)
                    LAST_YEAR -> offsetDateTime.minusYears(1)
                    ALL -> offsetDateTime.minusYears(TEN_YEARS)
                }
            }
}
