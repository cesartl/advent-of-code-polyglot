package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day24Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2023, 24)

    @Test
    fun solve1() {
        println(Day24.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day24.solve2(puzzleInput))
    }
}
