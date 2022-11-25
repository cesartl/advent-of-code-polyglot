package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day12Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2022, 12)

    @Test
    fun solve1() {
        println(Day12.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day12.solve2(puzzleInput))
    }
}