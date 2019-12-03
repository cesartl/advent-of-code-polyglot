package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day2Test {

    val input = InputUtils.getString(2019, 2).split(",").map { it.toInt() }.toIntArray()

    @Test
    fun solve1() {
        println(Day2.solve1(input))
    }

    @Test
    internal fun executeProgram() {
        assertThat(Day2.executeProgram(intArrayOf(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50)).toList())
                .containsExactly(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50)
        assertThat(Day2.executeProgram(intArrayOf(2, 4, 4, 5, 99, 0)).toList())
                .containsExactly(2, 4, 4, 5, 99, 9801)
        assertThat(Day2.executeProgram(intArrayOf(1, 1, 1, 4, 99, 5, 6, 0, 99)).toList())
                .containsExactly(30, 1, 1, 4, 2, 5, 6, 0, 99)
    }

    @Test
    internal fun solve2() {
        println(Day2.solve2(input))
    }
}