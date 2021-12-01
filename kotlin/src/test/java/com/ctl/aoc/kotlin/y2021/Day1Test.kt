package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day1Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2021, 1)

    val example = """199
200
208
210
200
207
240
269
260
263"""

    @Test
    fun solve1() {
        println(Day1.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day1.solve2(example.splitToSequence("\n")))
        println(Day1.solve2(puzzleInput))
    }
}