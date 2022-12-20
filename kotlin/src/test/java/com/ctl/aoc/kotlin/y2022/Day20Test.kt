package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
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
        assertEquals(23321, Day20.solve1(puzzleInput))
    }

    @Test
    fun solve1Bis() {
        println(Day20.solve1Bis(exampleInput))
        assertEquals(23321, Day20.solve1Bis(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day20.solve2(exampleInput))
        println(Day20.solve2(puzzleInput))
    }

    @Test
    fun solve2Bis() {
        println(Day20.solve2Bis(exampleInput))
        assertEquals(1428396909280, Day20.solve2Bis(puzzleInput))
    }
}
