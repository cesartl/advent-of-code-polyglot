package com.ctl.aoc.kotlin.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class RPNTest{

    @Test
    fun rpnParse() {
        assertRpn("1 + 2 + 3 * 5", 18)
        assertRpn("9 - 5", 4)
        assertRpn("3 * (8 + 2)", 30)
    }

    fun assertRpn(input: String, expected: Long){
        val eval = RPN.parse(input, longContext).eval()
        assertEquals(expected, eval, "$input should be equal to $expected but found $eval")
    }
}