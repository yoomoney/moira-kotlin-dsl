package ru.yoomoney.tech.moira.dsl.triggers.expression

import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import ru.yoomoney.tech.moira.dsl.triggers.TriggerState.ERROR
import ru.yoomoney.tech.moira.dsl.triggers.TriggerState.OK
import ru.yoomoney.tech.moira.dsl.triggers.TriggerState.WARN

class AdvancedModeTriggerExpressionTest {

    @Test
    fun `should create advanced mode trigger expression`() {
        // given
        val build: AdvancedModeTriggerExpression.Builder.() -> String = {
            "t1 > 100 && $PREV_STATE == $OK ? $WARN : $ERROR"
        }

        // when
        val expression = AdvancedModeTriggerExpression.Builder.build()
        val advancedExpression = AdvancedModeTriggerExpression.Builder.asExpression(expression)

        // then
        JSONAssert.assertEquals(
            """
            {
              "trigger_type": "expression",
              "expression": "t1 > 100 && PREV_STATE == OK ? WARN : ERROR"
            }
            """.trimIndent(),
            advancedExpression.toJson().toString(),
            true
        )
    }
}
