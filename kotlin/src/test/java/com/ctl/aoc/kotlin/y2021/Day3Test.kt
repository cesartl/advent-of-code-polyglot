package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day3Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2021, 3)

    val example = """00100
11110
10110
10111
10101
01111
00111
11100
10000
11001
00010
01010""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day3.solve1(example))
        println(Day3.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day3.solve2(example))
        println(Day3.solve2(puzzleInput))
    }
}