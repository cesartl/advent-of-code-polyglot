package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day17Test {

    val puzzleInput = InputUtils.getLines(2020, 17)

    val example = """.#.
..#
###""".splitToSequence("\n")

    @Test
    internal fun p() {
        val p = Day17.Position(0, 0, 0)
        val n = p.neighbours().toList()
        println(n.size)
        println(n)
    }

    @Test
    fun solve1() {
        println(Day17.solve1(example))
        println(Day17.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        Day17.solve2(puzzleInput)
    }
}