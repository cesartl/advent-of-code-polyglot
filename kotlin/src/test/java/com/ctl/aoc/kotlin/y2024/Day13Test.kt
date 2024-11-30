package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day13Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2024, 13)

    @Test
    fun solve1() {
        println(Day13.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day13.solve2(puzzleInput))
    }
}
