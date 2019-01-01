package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day3Test {

    val input = InputUtils.getString(2015, 3)

    @Test
    fun solve1() {
        assertEquals(2, Day3.solve1(">"))
        assertEquals(4, Day3.solve1("^>v<"))
        assertEquals(2, Day3.solve1("^v^v^v^v^v"))
        println(Day3.solve1(input))
    }

    @Test
    fun solve2() {
        assertEquals(3, Day3.solve2("^>"))
        assertEquals(3, Day3.solve2("^>v<"))
        assertEquals(11, Day3.solve2("^v^v^v^v^v"))
        println(Day3.solve2(input))
    }
}