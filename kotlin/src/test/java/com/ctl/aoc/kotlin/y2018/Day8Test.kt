package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day8Test{
    val input = InputUtils.getString("2018", "day8.txt")
    val example = "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2"

    @Test
    internal fun testPart1() {

        assertEquals(138, Day8.solve1(example))
        println(Day8.solve1(input))
    }

    @Test
    internal fun testPart2() {
        assertEquals(66, Day8.solve2(example))
        println(Day8.solve2(input))
    }

    @Test
    internal fun name() {
        var x = (1 shl 3).inv()
        println(13.toString(2))
        println((13 and x).toString(2))
    }
}