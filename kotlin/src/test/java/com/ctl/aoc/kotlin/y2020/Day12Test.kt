package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day12Test {

    val puzzleInput = InputUtils.getLines(2020, 12)

    val example = """F10
N3
F7
R90
F11""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day12.solve1(example))
        println(Day12.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day12.solve2(example))
        println(Day12.solve2(puzzleInput))
    }
}