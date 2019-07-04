package ru.yandex.money.moira.dsl.triggers.expression

import org.json.JSONObject
import ru.yandex.money.moira.dsl.triggers.MoiraDsl
import ru.yandex.money.moira.dsl.triggers.json.json

/**
 * A trigger expression in [advanced mode](https://moira.readthedocs.io/en/latest/user_guide/advanced.html).
 */
class AdvancedModeTriggerExpression private constructor(private val expression: String) : TriggerExpression {

    override val type: TriggerType = TriggerType.EXPRESSION

    override fun toJson(): JSONObject = json {
        "trigger_type" to type.type
        "expression" to expression
    }

    /**
     * Single instance of builder for [AdvancedModeTriggerExpression].
     */
    @MoiraDsl
    object Builder {

        /**
         * Previous trigger state that can be used in expressions.
         */
        const val PREV_STATE = "PREV_STATE"

        /**
         * Creates a new instance of [AdvancedModeTriggerExpression]. It's highly recommended to use [] for state
         * constants to avoid typos.
         */
        fun asExpression(expression: String) = AdvancedModeTriggerExpression(expression = expression)
    }
}
