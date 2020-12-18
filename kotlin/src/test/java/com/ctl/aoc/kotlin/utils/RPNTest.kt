package com.ctl.aoc.kotlin.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class RPNTest{

    @Test
    fun rpnParse() {
        assertRpn("1 + 2 + 3 * 5", 18)
        assertRpn("9 - 5", 4)
        assertRpn("3 * (8 + 2)", 30)
        assertRpn("10 * (10 + 2)", 120)
    }

    fun assertRpn(input: String, expected: Long){
        val rpn = RPN.parse(input, longContext)
        val eval = rpn.value
        assertEquals(expected, eval, "$input should be equal to $expected with $rpn")
    }
}