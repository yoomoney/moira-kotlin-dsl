package ru.yoomoney.tech.moira.dsl.triggers.targets

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TargetDelegateTest {

    @Test
    fun `should create target local variable delegate`() {
        // given
        val targets = mutableListOf<Target>()

        // when
        val target by TargetDelegate(metric = "metric", targets = targets)

        // then
        assertEquals("metric", target.metric)
        assertEquals("t1", target.toString())
        assertEquals(1, targets.size)
    }
}
