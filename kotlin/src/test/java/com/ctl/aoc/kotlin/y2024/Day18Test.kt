package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day18Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2024, 18)

    @Test
    fun solve1() {
        println(Day18.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day18.solve2(puzzleInput))
    }
}
