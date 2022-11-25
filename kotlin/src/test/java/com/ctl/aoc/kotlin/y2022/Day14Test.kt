package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day14Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2022, 14)

    @Test
    fun solve1() {
        println(Day14.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day14.solve2(puzzleInput))
    }
}