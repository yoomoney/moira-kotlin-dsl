package ru.yandex.money.moira.dsl.triggers.ttl

import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import ru.yandex.money.moira.dsl.triggers.TriggerState.ERROR
import java.time.Duration

class TtlTest {

    @Test
    fun `should create time-to-live parameters`() {
        // given
        val builder = Ttl.Builder()

        // when
        val build: Ttl.Builder.() -> Unit = {
            ttl = Duration.ofMinutes(5)
            state = ERROR
            muteNewMetrics = true
        }
        builder.build()
        val ttl = builder.asTtl()

        // then
        JSONAssert.assertEquals(
            """
            {
              "ttl": 300,
              "ttl_state": "ERROR",
              "mute_new_metrics": true
            }
            """.trimIndent(),
            ttl.toJson().toString(),
            true
        )
    }
}
