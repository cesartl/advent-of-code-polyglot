package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day6Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2023, 6)
    val example = """
        Time:      7  15   30
        Distance:  9  40  200
    """.splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day6.solve1(example))
        println(Day6.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day6.solve2(example))
        println(Day6.solve2(puzzleInput))
    }
}
