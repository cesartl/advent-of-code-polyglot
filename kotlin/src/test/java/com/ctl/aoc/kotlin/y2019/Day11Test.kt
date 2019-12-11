package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day11Test {

    val puzzleInput = InputUtils.getString(2019, 11).split(",").map { it.toLong() }.toLongArray()

    @Test
    fun solve1() {
        println(Day11.solve1(puzzleInput = puzzleInput)) // not 2673, too high
    }

    @Test
    fun solve2() {
        println(Day11.solve2(puzzleInput = puzzleInput)) // not 2673, too high
    }
}