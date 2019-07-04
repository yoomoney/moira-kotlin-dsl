package ru.yandex.money.moira.dsl.triggers.schedule

import org.json.JSONObject
import ru.yandex.money.moira.dsl.triggers.json.Json
import ru.yandex.money.moira.dsl.triggers.json.json
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

/**
 * Represents a day in watch time schedule.
 */
class Day(
    /**
     * A day of the week.
     */
    private val day: DayOfWeek,
    /**
     * Indicates whether notifications can be sent on this [day].
     */
    private val enabled: Boolean = true
) : Json {

    override fun toJson(): JSONObject = json {
        "name" to day.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.ENGLISH)
        "enabled" to enabled
    }
}
