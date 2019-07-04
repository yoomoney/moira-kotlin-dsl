package ru.yandex.money.moira.dsl.triggers

/**
 * The state of the trigger.
 */
enum class TriggerState(val state: String) {

    /**
     * Trigger is in OK state.
     */
    OK("OK"),

    /**
     * Trigger is in WARN state.
     */
    WARN("WARN"),

    /**
     * Trigger is in ERROR state.
     */
    ERROR("ERROR"),

    /**
     * No data available for given trigger.
     */
    NO_DATA("NODATA")
}
