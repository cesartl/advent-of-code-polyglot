package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day11Test {

    val puzzleInput = InputUtils.downloadAndGetString(2024, 11).trim().splitToSequence(" ")
    val example = "125 17".splitToSequence(" ")

    @Test
    fun solve1() {
        println(Day11.solve1(example))
        println(Day11.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day11.solve2(example))
        println(Day11.solve2(puzzleInput))
    }
}
