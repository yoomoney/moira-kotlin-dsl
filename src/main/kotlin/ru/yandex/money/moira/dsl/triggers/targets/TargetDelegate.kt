package ru.yandex.money.moira.dsl.triggers.targets

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Delegate provider for [Target] local variables.
 */
class TargetDelegate(private val metric: String, private val targets: MutableList<Target>) {

    operator fun provideDelegate(thisRef: Nothing?, property: KProperty<*>): ReadOnlyProperty<Nothing?, Target> {
        val target = Target("t${targets.size + 1}", metric)
        targets += target
        return object : ReadOnlyProperty<Nothing?, Target> {
            override fun getValue(thisRef: Nothing?, property: KProperty<*>): Target = target
        }
    }
}
