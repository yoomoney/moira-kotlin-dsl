package ru.yoomoney.tech.moira.dsl.triggers.json

import org.json.JSONObject
import java.lang.IllegalArgumentException

/**
 * Builder for JSON objects.
 *
 * **Note that** given [json] will be mutated via extension functions.
 */
class JsonBuilder(val json: JSONObject) {

    infix fun String.to(value: Json) {
        json.put(this, value.toJson())
    }

    infix fun String.to(value: String) {
        json.put(this, value)
    }

    infix fun String.to(value: Double) {
        json.put(this, value)
    }

    infix fun String.to(value: Boolean) {
        json.put(this, value)
    }

    infix fun String.to(value: Int) {
        json.put(this, value)
    }

    infix fun String.to(value: Long) {
        json.put(this, value)
    }

    infix fun String.to(values: List<*>) {
        json.put(this, values.map {
            when (it) {
                is String -> it
                is Json -> it.toJson()
                else -> throw IllegalArgumentException("Unknown item type: ${it?.let { any -> any::class }}")
            }
        })
    }

    operator fun Json.unaryPlus() {
        json.embed(this.toJson())
    }
}
