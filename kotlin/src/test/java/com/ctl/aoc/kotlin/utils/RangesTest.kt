package com.ctl.aoc.kotlin.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class RangesTest {

    @Test
    fun intersect() {
        assertEquals(4..5, (1..5).rangeIntersect((4..9)))
        assertEquals(4..5, (4..9).rangeIntersect((1..5)))
        assertEquals(2..4, (1..10).rangeIntersect((2..4)))
        assertEquals(null, (1..3).rangeIntersect((4..10)))
    }
}