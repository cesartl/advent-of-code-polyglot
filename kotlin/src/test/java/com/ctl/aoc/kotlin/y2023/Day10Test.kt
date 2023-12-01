package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day10Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2023, 10)

    @Test
    fun solve1() {
        println(Day10.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day10.solve2(puzzleInput))
    }
}
