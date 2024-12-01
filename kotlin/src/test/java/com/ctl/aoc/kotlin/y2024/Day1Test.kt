package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day1Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2024, 1)

    val example = """3   4
4   3
2   5
1   3
3   9
3   3""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day1.solve1(example))
        println(Day1.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day1.solve2(example))
        println(Day1.solve2(puzzleInput))
    }
}
