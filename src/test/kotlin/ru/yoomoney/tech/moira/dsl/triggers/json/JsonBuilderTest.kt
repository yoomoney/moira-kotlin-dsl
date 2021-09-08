package ru.yoomoney.tech.moira.dsl.triggers.json

import org.json.JSONObject
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class JsonBuilderTest {

    @ParameterizedTest
    @MethodSource("unknownItems")
    fun `should throw an exception when type of items list is unknown`(items: List<*>) {
        // given
        val builder = JsonBuilder(JSONObject())

        // when
        val build: JsonBuilder.() -> Unit = {
            "items" to items
        }
        assertThrows<IllegalArgumentException> { builder.build() }
    }

    companion object {

        @Suppress("unused")
        @JvmStatic
        fun unknownItems() = Stream.of(
            Arguments.of(listOf(1, 2, 3)),
            Arguments.of(listOf(null, null, null))
        )
    }
}
