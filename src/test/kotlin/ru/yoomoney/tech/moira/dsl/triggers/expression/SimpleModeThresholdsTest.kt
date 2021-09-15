package ru.yoomoney.tech.moira.dsl.triggers.expression

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SimpleModeThresholdsTest {

    @Test
    fun `should throw an exception when warn and error thresholds are both null`() {
        // expect
        assertThrows<IllegalStateException> { SimpleModeThresholds(warn = null, error = null) }
    }
}
