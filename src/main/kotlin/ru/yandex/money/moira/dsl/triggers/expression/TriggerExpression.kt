package ru.yandex.money.moira.dsl.triggers.expression

import ru.yandex.money.moira.dsl.triggers.MoiraDsl
import ru.yandex.money.moira.dsl.triggers.json.Json

/**
 * Represents an expression that is used to define the trigger condition.
 */
interface TriggerExpression : Json {

    /**
     * A type of trigger expression.
     */
    val type: TriggerType

    /**
     * Builder for [TriggerExpression].
     */
    @MoiraDsl
    class Builder {

        /**
         * Creates an expression in simple mode.
         */
        fun simple(build: SimpleModeTriggerExpression.Builder.() -> SimpleModeTriggerExpression): TriggerExpression {
            val builder = SimpleModeTriggerExpression.Builder()
            return builder.build()
        }

        /**
         * Creates an expression in advanced mode.
         */
        fun advanced(build: AdvancedModeTriggerExpression.Builder.() -> String): TriggerExpression {
            val expression = AdvancedModeTriggerExpression.Builder.build()
            return AdvancedModeTriggerExpression.Builder.asExpression(expression)
        }
    }
}
