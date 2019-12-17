package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day17Test {

    val puzzleInput = InputUtils.getString(2019, 17).split(",").map { it.toLong() }.toLongArray()

    @Test
    fun solve1() {
        println(Day17.solve1(puzzleInput))
    }
}