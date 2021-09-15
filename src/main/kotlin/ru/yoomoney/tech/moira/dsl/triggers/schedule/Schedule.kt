package ru.yoomoney.tech.moira.dsl.triggers.schedule

import org.json.JSONObject
import ru.yoomoney.tech.moira.dsl.triggers.MoiraDsl
import ru.yoomoney.tech.moira.dsl.triggers.json.Json
import ru.yoomoney.tech.moira.dsl.triggers.json.json
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.util.TimeZone
import java.util.concurrent.TimeUnit

/**
 * A configuration of watch time for your trigger. All notifications will be disabled if they sent out of watch time.
 */
class Schedule(
    /**
     * A start of watch time period of each day.
     *
     * [start] can be greater than [end]. It means that your watch time will be: from [start] of one day to [end] of
     * next day.
     */
    private val start: LocalTime = LocalTime.MIN,
    /**
     * An end of watch time period of each day.
     *
     * [start] can be greater than [end]. It means that your watch time will be: from [start] of one day to [end] of
     * next day.
     */
    private val end: LocalTime = LocalTime.MAX.truncatedTo(ChronoUnit.MINUTES),
    /**
     * Time zone offset of watch time period.
     */
    private val timeZoneOffset: ZoneOffset = ZoneOffset.ofTotalSeconds(0),
    /**
     * List of days with information about enabled/disabled.
     */
    private val days: List<Day> = DayOfWeek.values().map { Day(it) }
) : Json {

    override fun toJson(): JSONObject = json {
        "startOffset" to start.get(ChronoField.MINUTE_OF_DAY)
        "endOffset" to end.get(ChronoField.MINUTE_OF_DAY)
        "tzOffset" to TimeUnit.SECONDS.toMinutes(timeZoneOffset.totalSeconds.toLong())
        "days" to days
    }

    /**
     * Builder for [Schedule].
     */
    @MoiraDsl
    class Builder {

        private val disabledDays: MutableSet<DayOfWeek> = mutableSetOf()

        /**
         * Watch time period.
         *
         * Watch time is defined by pair of local time instances.
         *
         * All day by default.
         */
        var watchTime: Pair<LocalTime, LocalTime> = LocalTime.MIN to LocalTime.MAX.truncatedTo(ChronoUnit.MINUTES)

        /**
         * Zone offset of watch time period.
         *
         * System default by default.
         */
        var zoneOffset: ZoneOffset = ZoneOffset.ofHours(
            TimeUnit.MILLISECONDS.toHours(TimeZone.getDefault().rawOffset.toLong()).toInt()
        )

        /**
         * Disables this day for watching.
         */
        operator fun DayOfWeek.unaryMinus() {
            disabledDays += this
        }

        /**
         * Creates watch time period.
         */
        operator fun String.rangeTo(end: String): Pair<LocalTime, LocalTime> =
            LocalTime.parse(this, DateTimeFormatter.ISO_LOCAL_TIME).truncatedTo(ChronoUnit.MINUTES) to
                LocalTime.parse(end, DateTimeFormatter.ISO_LOCAL_TIME).truncatedTo(ChronoUnit.MINUTES)

        /**
         * Creates a schedule.
         */
        fun asSchedule() = Schedule(
            start = watchTime.first,
            end = watchTime.second,
            timeZoneOffset = zoneOffset,
            days = DayOfWeek.values().map { Day(it, it !in disabledDays) }
        )
    }
}
