package com.ctl.aoc.kotlin.y2016

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day24Test {

    val input = InputUtils.getLines(2016, 24)

    val example = """###########
#0.1.....2#
#.#######.#
#4.......3#
###########""".split("\n").asSequence()

    @Test
    fun solve1() {
        assertEquals(14, Day24.solve1(example))
        println(Day24.solve1(input))
    }

    @Test
    fun solve2() {
        println(Day24.solve2(input))
    }
}