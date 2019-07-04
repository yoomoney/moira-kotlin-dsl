package ru.yandex.money.moira.dsl.triggers.expression

/**
 * A type of trigger expression.
 */
enum class TriggerType(val type: String) {

    /**
     * A notification is sent if the metric value is greater than the specified threshold.
     */
    RISING("rising"),

    /**
     * A notification is sent if the metric value is less than the specified threshold.
     */
    FALLING("falling"),

    /**
     * A condition to send notification is defined by govaluate expression.
     */
    EXPRESSION("expression")
}
