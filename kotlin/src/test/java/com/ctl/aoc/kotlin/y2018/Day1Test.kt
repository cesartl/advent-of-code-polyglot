package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.InputUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class  Day1Test{

    @Test
    fun day1Part1() {
        val lines = InputUtils.getLines("2018", "day1.txt")
        println(Day1p1.solve(lines))
    }

    @Test
    fun part2() {
        assertThat(Day1p2.solve(listOf(1, -1))).isEqualTo(0)
        assertThat(Day1p2.solve(listOf(3, 3, 4, -2, -4))).isEqualTo(10)
        assertThat(Day1p2.solve(listOf(-6, 3, 8, 5, -6))).isEqualTo(5)
        assertThat(Day1p2.solve(listOf(7, 7, -2, -7, -4))).isEqualTo(14)
        val lines = InputUtils.getLines("2018", "day1.txt")
        val x = Day1p2.solve(lines.map { it.toInt() })
        println(x)
    }
}