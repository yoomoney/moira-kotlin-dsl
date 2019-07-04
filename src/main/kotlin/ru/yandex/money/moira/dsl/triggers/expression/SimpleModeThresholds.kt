package ru.yandex.money.moira.dsl.triggers.expression

import ru.yandex.money.moira.dsl.triggers.MoiraDsl

/**
 * The set of threshold for expression mode conditions.
 */
data class SimpleModeThresholds(
    /**
     * WARN level threshold.
     *
     * May be `null` if *warn* notification is unnecessary.
     * Note that at least one of [warn] and [error] thresholds must be set.
     */
    val warn: Double?,
    /**
     * ERROR level threshold.
     *
     * May be `null` if *error* notification is unnecessary.
     * Note that at least one of [warn] and [error] thresholds must be set.
     */
    val error: Double?
) {

    init {
        if (warn == null && error == null) {
            throw IllegalStateException("At least one of 'warn' or 'error' thresholds must be provided")
        }
    }

    /**
     * Builder for [SimpleModeThresholds].
     */
    @MoiraDsl
    class Builder {

        /**
         * See [SimpleModeThresholds.warn]
         */
        var warn: Double? = null

        /**
         * See [SimpleModeThresholds.error]
         */
        var error: Double? = null

        /**
         * Creates a [SimpleModeThresholds] object. This method should be called after [warn] and/or [error] properties
         * are set.
         */
        fun asThresholds() = SimpleModeThresholds(warn = warn, error = error)
    }
}
