package com.ctl.aoc.kotlin.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MatrixKtTest {
    @Test
    internal fun identity() {
        val col = Matrix21(3, 5)
        assertEquals(col, Matrix22.identity x col)
    }

    @Test
    internal fun rotate() {
        val col = Matrix21(1, 0)
        assertEquals(Matrix21(0, 1), Matrix22.rotate90 x col)
        assertEquals(Matrix21(-1, 0), Matrix22.rotate180 x col)
        assertEquals(Matrix21(0, -1), Matrix22.rotate270 x col)

        val waypoint = Matrix21(10, -4)
        println(Matrix22.rotate270 x waypoint)
    }
}