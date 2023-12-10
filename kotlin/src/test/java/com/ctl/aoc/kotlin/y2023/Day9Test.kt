package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day9Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2023, 9)

    val example = """0 3 6 9 12 15
1 3 6 10 15 21
10 13 16 21 30 45""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day9.solve1(example))
        println(Day9.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day9.solve2(example))
        println(Day9.solve2(puzzleInput))
    }
}
