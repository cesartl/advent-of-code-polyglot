package com.ctl.aoc.kotlin.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MapsTest {
    @Test
    internal fun testMerge() {
        val m1 = mapOf("a" to 3, "b" to 5)
        val m2 = mapOf("b" to 7, "c" to 11)
        assertEquals(mapOf("a" to 3, "b" to 12, "c" to 11),m1.merge(m2) { x, y -> x + y })
    }
}