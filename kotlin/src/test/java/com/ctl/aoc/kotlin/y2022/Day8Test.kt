package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day8Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2022, 8)

    @Test
    fun solve1() {
        println(Day8.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day8.solve2(puzzleInput))
    }
}