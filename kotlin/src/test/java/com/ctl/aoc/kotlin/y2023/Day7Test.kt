package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day7Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2023, 7)

    val example = """32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day7.solve1(example))
        println(Day7.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day7.solve2(puzzleInput))
    }
}
