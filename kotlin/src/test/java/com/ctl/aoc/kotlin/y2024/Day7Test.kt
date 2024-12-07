package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day7Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2024, 7)

    val example = """190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day7.solve1(example))
        println(Day7.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day7.solve2(example))
        println(Day7.solve2(puzzleInput))
    }
}
