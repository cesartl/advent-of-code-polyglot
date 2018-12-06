package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day6Test{

    val input = InputUtils.getLines("2018", "day6.txt")
    val example = """1, 1
1, 6
8, 3
3, 4
5, 5
8, 9""".split("\n").asSequence()

    @Test
    internal fun tesSolve1() {
        assertEquals(17, Day6.solve1(example))
        println(Day6.solve1(input))
    }

    @Test
    internal fun tesSolve2() {
        assertEquals(16, Day6p2.solve2(example, 32))
        println(Day6p2.solve2(input, 10000))
    }
}