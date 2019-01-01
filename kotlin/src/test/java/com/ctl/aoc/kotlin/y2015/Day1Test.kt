package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day1Test {

    val input = InputUtils.getString(2015, 1)

    @Test
    fun solve1() {
        assertEquals(3, Day1.solve1("))((((("))
        println(Day1.solve1(input))
    }

    @Test
    fun solve2() {
        assertEquals(5, Day1.solve2("()())"))
        println(Day1.solve2(input))
    }
}