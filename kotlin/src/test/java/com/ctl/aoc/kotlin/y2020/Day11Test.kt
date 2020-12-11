package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day11Test {

    val puzzleInput = InputUtils.getLines(2020, 11)

    val example = """L.LL.LL.LL
LLLLLLL.LL
L.L.L..L..
LLLL.LL.LL
L.LL.LL.LL
L.LLLLL.LL
..L.L.....
LLLLLLLLLL
L.LLLLLL.L
L.LLLLL.LL""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day11.solve1(example))
        println(Day11.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        val g = Day11.parseGrid(example)
        println(Day11.solve2(example))
        println(Day11.solve2(puzzleInput))
    }
}