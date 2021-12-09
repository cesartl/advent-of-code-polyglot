package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day9Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2021, 9)

    val example = """2199943210
3987894921
9856789892
8767896789
9899965678""".splitToSequence("\n")

    @Test
    fun solve1() {
        println("abc".splitToSequence("").toList())
        println(Day9.solve1(example))
        println(Day9.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day9.solve2(example))
        println(Day9.solve2(puzzleInput))

        println(Day9.solve2Bis(example))
        println(Day9.solve2Bis(puzzleInput))
    }
}