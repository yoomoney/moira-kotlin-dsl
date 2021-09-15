package ru.yoomoney.tech.moira.dsl.triggers.targets

/**
 * Represents targets metric.
 */
class Target(
    /**
     * ID of metric that used to reference in expressions.
     *
     * Has format `t[\d+]`
     */
    private val id: String,
    /**
     * Target metric in graphite syntax.
     */
    val metric: String
) {

    /**
     * This implementation used when interpolating advanced expressions.
     */
    override fun toString(): String = id
}
