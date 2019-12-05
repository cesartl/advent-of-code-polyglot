package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class Day5Test {

    val puzzleInput = InputUtils.getString(2019, 5).split(",").map { it.toInt() }.toIntArray()

    @Test
    internal fun test1() {
        val intCode = intArrayOf(1002, 4, 3, 4, 33)
        val state = Day5.IntCodeState(intCode = intCode)
        Day5.run {
            val final = state.execute()
            println("")
        }
    }

    @Test
    fun solve1() {
        assertThat(Day5.solve1(puzzleInput)).isEqualTo(13087969)
    }

    @Test
    fun solve2() {
        assertThat(Day5.solve2(puzzleInput)).isEqualTo(14110739)
    }
}