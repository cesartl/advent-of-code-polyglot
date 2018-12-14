package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day14Test {
    val input = 846601

    @Test
    internal fun testDigits() {
        assertEquals(listOf(1, 0), Day14.digits(10).reversed())
        assertEquals(listOf(8), Day14.digits(8).reversed())
        assertEquals(listOf(1, 2, 3, 4, 5), Day14.digits(12345).reversed())
    }

    @Test
    internal fun testSolve1() {
        assertEquals("5158916779", Day14.solve1(9, true))
        assertEquals("0124515891", Day14.solve1(5))
        assertEquals("9251071085", Day14.solve1(18))
        assertEquals("5941429882", Day14.solve1(2018, debug = false))
        assertEquals("3811491411", Day14.solve1(input)) // not 2151055104
    }

    @Test
    internal fun testSolve2() {
        assertEquals(9, Day14.solve2("51589"))
        assertEquals(5, Day14.solve2("01245"))
        assertEquals(18, Day14.solve2("92510"))
        assertEquals(2018, Day14.solve2("59414"))
        assertEquals(20408083, Day14.solve2(input.toString())) //not 20408084
    }
}