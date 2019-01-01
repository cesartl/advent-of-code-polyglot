package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day2Test {

    val input = InputUtils.getLines(2015, 2)

    @Test
    fun solve1() {
        assertEquals(58, Day2.Present(listOf(2, 3, 4)).total)
        assertEquals(43, Day2.Present(listOf(1, 1, 10)).total)
        println(Day2.solve1(input))
    }

    @Test
    internal fun solve2() {
        assertEquals(34, Day2.Present(listOf(2, 3, 4)).ribbon)
        assertEquals(14, Day2.Present(listOf(1, 1, 10)).ribbon)
        println(Day2.solve2(input))
    }
}