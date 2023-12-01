package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day1Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2023, 1)

    val example = """1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet""".splitToSequence("\n")

    val example2 = """two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day1.solve1(example))
        println(Day1.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day1.solve2(example2))
        println(Day1.solve2(puzzleInput))
    }
}
