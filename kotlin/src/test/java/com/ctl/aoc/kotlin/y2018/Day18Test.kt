package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day18Test {
    val input = InputUtils.getLines("2018", "day18.txt").toList()

    val example = """.#.#...|#.
.....#|##|
.|..|...#.
..|#.....#
#.#|||#|#|
...#.||...
.|....|...
||...#|.#|
|.||||..|.
...#.|..|.""".split("\n")

    @Test
    internal fun testSolve1() {
        assertEquals(1147, Day18.solve1(example, 10))
        println(Day18.solve1(input.toList(), 10))
    }

    @Test
    internal fun testSolve2() {
//        assertEquals(Day18.solve1(input,700), Day18.solve2(input, 700))
//        assertEquals(Day18.solve1(input,1000), Day18.solve2(input, 1000))
        println(Day18.solve2(input.toList(), 1000000000))
    }

    @Test
    internal fun cycle() {
        Day18.visualiseCycle(input)
    }
}