package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day13Test {

    val puzzleInput = InputUtils.getString(2019, 13).split(",").map { it.toLong() }.toLongArray()

    @Test
    fun solve1() {
        println(Day13.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day13.solve2(puzzleInput))
    }
}