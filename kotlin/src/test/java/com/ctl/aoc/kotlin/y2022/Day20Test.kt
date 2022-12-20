package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day20Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2022, 20)

    val exampleInput = """1
2
-3
3
-2
0
4""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day20.solve1(exampleInput))
        println(Day20.solve1(puzzleInput))
        //>2785
    }

    @Test
    fun solve2() {
        println(Day20.solve2(puzzleInput))
    }
}
