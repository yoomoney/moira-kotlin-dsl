package ru.yandex.money.moira.dsl.triggers.expression

import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.skyscreamer.jsonassert.JSONAssert
import ru.yandex.money.moira.dsl.triggers.targets.Target
import java.util.stream.Stream

class SimpleModeTriggerExpressionTest {

    @ParameterizedTest
    @MethodSource("validRising")
    fun `should create rising trigger expression`(expectedWarn: Double?, expectedError: Double?, expectedJson: String) {
        // given
        val builder = SimpleModeTriggerExpression.Builder()
        val t1 = Target(id = "t1", metric = "Test")

        // when
        val expression = with(builder) {
            t1 rising {
                warn = expectedWarn
                error = expectedError
            }
        }

        // then
        JSONAssert.assertEquals(expectedJson, expression.toJson().toString(), true)
    }

    @ParameterizedTest
    @MethodSource("invalidRising")
    fun `should throw an exception when creating rising trigger expression with invalid thresholds`(
        expectedWarn: Double?,
        expectedError: Double?
    ) {
        // given
        val builder = SimpleModeTriggerExpression.Builder()
        val t1 = Target(id = "t1", metric = "Test")

        // when
        assertThrows<IllegalStateException> {
            with(builder) {
                t1 rising {
                    warn = expectedWarn
                    error = expectedError
                }
            }
        }
    }

    @ParameterizedTest
    @MethodSource("validFalling")
    fun `should create falling trigger expression`(expectedWarn: Double?, expectedError: Double?, expectedJson: String) {
        // given
        val builder = SimpleModeTriggerExpression.Builder()
        val t1 = Target(id = "t1", metric = "Test")

        // when
        val expression = with(builder) {
            t1 falling {
                warn = expectedWarn
                error = expectedError
            }
        }

        // then
        JSONAssert.assertEquals(expectedJson, expression.toJson().toString(), true)
    }

    @ParameterizedTest
    @MethodSource("invalidFalling")
    fun `should throw an exception when creating falling trigger expression with invalid thresholds`(
        expectedWarn: Double?,
        expectedError: Double?
    ) {
        // given
        val builder = SimpleModeTriggerExpression.Builder()
        val t1 = Target(id = "t1", metric = "Test")

        // when
        assertThrows<IllegalStateException> {
            with(builder) {
                t1 falling {
                    warn = expectedWarn
                    error = expectedError
                }
            }
        }
    }

    @Suppress("unused")
    companion object {

        @JvmStatic
        fun validRising(): Stream<Arguments> = Stream.of(
            Arguments.of(
                10.0,
                100.0,
                """
                {
                  "trigger_type": "rising",
                  "warn_value": 10.0,
                  "error_value": 100.0
                }
                """.trimIndent()
            ),
            Arguments.of(
                10.0,
                null,
                """
                {
                  "trigger_type": "rising",
                  "warn_value": 10.0
                }
                """.trimIndent()
            ),
            Arguments.of(
                null,
                100.0,
                """
                {
                  "trigger_type": "rising",
                  "error_value": 100.0
                }
                """.trimIndent()
            )
        )

        @JvmStatic
        fun invalidRising(): Stream<Arguments> = Stream.of(
            Arguments.of(100.0, 10.0),
            Arguments.of(10.0, 10.0),
            Arguments.of(null, null)
        )

        @JvmStatic
        fun validFalling(): Stream<Arguments> = Stream.of(
            Arguments.of(
                100.0,
                10.0,
                """
                {
                  "trigger_type": "falling",
                  "warn_value": 100.0,
                  "error_value": 10.0
                }
                """.trimIndent()
            ),
            Arguments.of(
                100.0,
                null,
                """
                {
                  "trigger_type": "falling",
                  "warn_value": 100.0
                }
                """.trimIndent()
            ),
            Arguments.of(
                null,
                10.0,
                """
                {
                  "trigger_type": "falling",
                  "error_value": 10.0
                }
                """.trimIndent()
            )
        )

        @JvmStatic
        fun invalidFalling(): Stream<Arguments> = Stream.of(
            Arguments.of(10.0, 100.0),
            Arguments.of(10.0, 10.0),
            Arguments.of(null, null)
        )
    }
}
