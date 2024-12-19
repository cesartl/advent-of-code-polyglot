package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day19Test {

    val puzzleInput = InputUtils.downloadAndGetString(2024, 19)

    val example = """r, wr, b, g, bwu, rb, gb, br

brwrr
bggr
gbbr
rrbgbr
ubwu
bwurrg
brgr
bbrgwb
"""

    @Test
    fun solve1() {
        println(Day19.solve1(example))
        println(Day19.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day19.solve2(example))
        println(Day19.solve2(puzzleInput))
    }
}
