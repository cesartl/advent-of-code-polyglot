package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day17Test {

    val input = InputUtils.getLines(2015, 17).map { it.toInt() }.toList()

    @Test
    fun solve1() {
        assertEquals(4, Day17.solve1(listOf(20, 15, 10, 5, 5), 25))
        println(Day17.solve1(input, 150))
    }

    @Test
    fun solve2() {
        assertEquals(3, Day17.solve2(listOf(20, 15, 10, 5, 5), 25))
        println(Day17.solve2(input, 150))
    }
}