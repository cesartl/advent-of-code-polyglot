package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day8Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2022, 8)

    val testInput = """30373
25512
65332
33549
35390""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day8.solve1(testInput))
        println(Day8.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day8.solve2(testInput))
        println(Day8.solve2(puzzleInput))
    }
}
