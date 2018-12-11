package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day11Test{
    val input = 2187

    @Test
    internal fun testPowerLevel() {
       assertEquals(4, Day11.Cell(3, 5).powerLevel(8))
       assertEquals(-5, Day11.Cell(122, 79).powerLevel(57))
       assertEquals(0, Day11.Cell(217, 196).powerLevel(39))
       assertEquals(4, Day11.Cell(101, 153).powerLevel(71))
    }

    @Test
    internal fun testSolve1() {
        assertEquals((21 to 61), Day11.solve1(42))
        println(Day11.solve1(input))
    }

    @Test
    internal fun solve2() {
//        assertEquals(Day11.Area(90,269, 16), Day11.solve2(18))
//        assertEquals(Day11.Area(232,251, 12), Day11.solve2(43))
        println(Day11.solve2(input)) //233,40,13
    }
}