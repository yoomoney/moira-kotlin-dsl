package ru.yoomoney.tech.moira.dsl.triggers.expression

import org.json.JSONObject
import ru.yoomoney.tech.moira.dsl.triggers.MoiraDsl
import ru.yoomoney.tech.moira.dsl.triggers.json.json
import ru.yoomoney.tech.moira.dsl.triggers.targets.Target

/**
 * A set of thresholds and the condition under which a notification is sent.
 *
 * Simple mode conditions are defined by threshold values.
 */
class SimpleModeTriggerExpression private constructor(
    /**
     * Specifies the condition for sending a notification.
     */
    override val type: TriggerType,
    /**
     * Threshold for *warn* level notification.
     *
     * May be `null` if *warn* notification is unnecessary.
     * Note that at least one of [warnThreshold] and [errorThreshold] must be set.
     */
    private val warnThreshold: Double?,
    /**
     * Threshold for *error* level notification.
     *
     * May be `null` if *error* notification is unnecessary.
     * Note that at least one of [warnThreshold] and [errorThreshold] must be set.
     */
    private val errorThreshold: Double?
) : TriggerExpression {

    override fun toJson(): JSONObject = json {
        "trigger_type" to type.type
        if (warnThreshold != null) {
            "warn_value" to warnThreshold
        }
        if (errorThreshold != null) {
            "error_value" to errorThreshold
        }
    }

    /**
     * Builder for [SimpleModeTriggerExpression].
     */
    @MoiraDsl
    class Builder {

        /**
         * Create rising trigger expression.
         *
         * In this mode, a notification is sent if the metric value exceeds the threshold value.
         *
         * For example, we have given thresholds:
         * ```
         * t1 rising {
         *   warn = 10.0
         *   error = 100.0
         * }
         * ```
         * + If we have metric value **5.0 (x < 10.0 < 100.0)** then trigger state is `OK`
         * + If we have metric value **20.0 (10.0 < x < 100.0)** then trigger state is `WARN`
         * + If we have metric value **200.0 (10.0 < 100.0 < x)** then trigger state is `ERROR`
         *
         * Note that error threshold must be greater than warn threshold.
         */
        infix fun Target.rising(build: SimpleModeThresholds.Builder.() -> Unit): SimpleModeTriggerExpression {
            val builder = SimpleModeThresholds.Builder()
            builder.build()
            val (warn, error) = builder.asThresholds()
            if (warn != null && error != null && warn >= error) {
                throw IllegalStateException("'warn' threshold must be less than 'error' threshold in rising mode")
            }
            return SimpleModeTriggerExpression(type = TriggerType.RISING, warnThreshold = warn, errorThreshold = error)
        }

        /**
         * Create falling trigger expression.
         *
         * In this mode, a notification is sent if the metric value falls below the threshold value.
         *
         * For example, we have given thresholds:
         * ```
         * t1 falling {
         *   warn = 100.0
         *   error = 10.0
         * }
         * ```
         * + If we have metric value **5.0 (x < 10.0 < 100.0)** then trigger state is `ERROR`
         * + If we have metric value **20.0 (10.0 < x < 100.0)** then trigger state is `WARN`
         * + If we have metric value **200.0 (10.0 < 100.0 < x)** then trigger state is `OK`
         *
         * Note that error threshold must be less than warn threshold.
         */
        infix fun Target.falling(build: SimpleModeThresholds.Builder.() -> Unit): SimpleModeTriggerExpression {
            val builder = SimpleModeThresholds.Builder()
            builder.build()
            val (warn, error) = builder.asThresholds()
            if (warn != null && error != null && warn <= error) {
                throw IllegalStateException("'warn' threshold must be greater than 'error' threshold in falling mode")
            }
            return SimpleModeTriggerExpression(type = TriggerType.FALLING, warnThreshold = warn, errorThreshold = error)
        }
    }
}
