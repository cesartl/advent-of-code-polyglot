package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day14Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2022, 14)

    val exampleInput = """498,4 -> 498,6 -> 496,6
503,4 -> 502,4 -> 502,9 -> 494,9""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day14.solve1(exampleInput))
        println(Day14.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day14.solve2(exampleInput))
        println(Day14.solve2(puzzleInput))
    }
}
