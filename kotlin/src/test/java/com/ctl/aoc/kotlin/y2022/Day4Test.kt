package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day4Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2022, 4)

    val testInput = """2-4,6-8
2-3,4-5
5-7,7-9
2-8,3-7
6-6,4-6
2-6,4-8""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day4.solve1(testInput))
        println(Day4.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day4.solve2(testInput))
        println(Day4.solve2(puzzleInput))
    }
}
