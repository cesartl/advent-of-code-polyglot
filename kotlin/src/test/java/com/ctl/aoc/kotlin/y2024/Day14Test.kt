package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day14Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2024, 14)

    @Test
    fun solve1() {
        println(Day14.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day14.solve2(puzzleInput))
    }
}
