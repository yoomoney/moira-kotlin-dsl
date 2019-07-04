package ru.yandex.money.moira.dsl.triggers.schedule

import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import java.time.DayOfWeek
import java.time.ZoneOffset

class ScheduleTest {

    @Test
    fun `should create schedule`() {
        // given
        val builder = Schedule.Builder()

        // when
        val build: Schedule.Builder.() -> Unit = {
            watchTime = "18:00".."03:00"
            zoneOffset = ZoneOffset.ofHours(-3)

            -DayOfWeek.SATURDAY
            -DayOfWeek.SUNDAY
        }
        builder.build()
        val schedule = builder.asSchedule()

        // then
        JSONAssert.assertEquals(
            """
            {
              "startOffset": 1080,
              "endOffset": 180,
              "tzOffset": -180,
              "days": [
                {
                  "name": "Mon",
                  "enabled": true
                },
                {
                  "name": "Tue",
                  "enabled": true
                },
                {
                  "name": "Wed",
                  "enabled": true
                },
                {
                  "name": "Thu",
                  "enabled": true
                },
                {
                  "name": "Fri",
                  "enabled": true
                },
                {
                  "name": "Sat",
                  "enabled": false
                },
                {
                  "name": "Sun",
                  "enabled": false
                }
              ]
            }
            """.trimIndent(),
            schedule.toJson().toString(),
            true
        )
    }
}
