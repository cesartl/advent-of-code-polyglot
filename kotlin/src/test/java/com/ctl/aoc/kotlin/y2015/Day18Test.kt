package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day18Test {

    val input = InputUtils.getLines(2015, 18)

    val example = """.#.#.#
...##.
#....#
..#...
#.#..#
####..""".split("\n").asSequence()

    @Test
    fun solve1() {
        assertEquals(4, Day18.solve1(example, 4))
        println(Day18.solve1(input, 100))
    }

    @Test
    fun solve2() {
        assertEquals(17, Day18.solve2(example, 5))
        println(Day18.solve2(input, 100))
    }
}