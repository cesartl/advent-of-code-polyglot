package com.ctl.aoc.kotlin.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PositionTest {

    @Test
    fun `test normalise`() {
        assertEquals(Position(1, 1), Position(2, 2).normalise())
        assertEquals(Position(2, 1), Position(10, 5).normalise())
        assertEquals(Position(1, 0), Position(2, 0).normalise())
        assertEquals(Position(0, 1), Position(0, 2).normalise())
    }
}