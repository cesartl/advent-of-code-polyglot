package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day2Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2022, 2)

    val testInput = """A Y
B X
C Z""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day2.solve1(testInput))
        println(Day2.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day2.solve2(testInput))
        println(Day2.solve2(puzzleInput))
    }
}
