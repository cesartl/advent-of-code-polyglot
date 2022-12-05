package com.ctl.aoc.kotlin.utils

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class RangesKtTest {

    @Test
    fun includes() {
        assertTrue((1..4).includes(2..4))
        assertTrue((1..4).includes(1..4))
        assertTrue((4..6).includes(6..6))
        assertTrue((6..6).includes(6..6))

        assertFalse((1..3).includes(4..6))
        assertFalse((11..12).includes(12..20))
        assertFalse((4..6).includes(6..7))
    }
}
