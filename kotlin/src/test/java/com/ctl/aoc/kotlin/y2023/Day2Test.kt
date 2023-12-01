package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day2Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2023, 2)

    @Test
    fun solve1() {
        println(Day2.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day2.solve2(puzzleInput))
    }
}