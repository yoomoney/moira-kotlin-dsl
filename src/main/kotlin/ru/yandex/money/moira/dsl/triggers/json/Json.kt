package ru.yandex.money.moira.dsl.triggers.json

import org.json.JSONObject

/**
 * Represents an object that can be serialized to JSON object.
 */
interface Json {

    /**
     * Serialize this object to JSON.
     */
    fun toJson(): JSONObject
}

/**
 * Embeds [other] [JSONObject] to this [JSONObject].
 *
 * Note that this [JSONObject] will be mutated after calling this method.
 *
 * Note that if some key from [other] exists in this [JSONObject] too then value in this [JSONObject] will be
 * overwritten by value from [other].
 *
 * Example of embedding:
 * ```
 * JSONObject a = JSONObject("""{"key1": "val1", "key2": "val2"}""")
 * JSONObject b = JSONObject("""{"key2": "val3", "key4": "val4"}""")
 * a.embed(b) // a = {"key1": "val1", "key2": "val3", "key4": "val4"}
 * ```
 */
fun JSONObject.embed(other: JSONObject) {
    other.keySet().forEach { this.put(it, other[it]) }
}

/**
 * Builds JSON object.
 *
 * **Note that** given [baseJson] will be mutated via [JsonBuilder] extension functions.
 */
fun json(baseJson: JSONObject = JSONObject(), build: JsonBuilder.() -> Unit): JSONObject {
    val builder = JsonBuilder(baseJson)
    builder.build()
    return builder.json
}
