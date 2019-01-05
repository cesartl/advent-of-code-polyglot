package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day9Test {

    val input = InputUtils.getLines(2015, 9)

    val example = """London to Dublin = 464
London to Belfast = 518
Dublin to Belfast = 141""".split("\n").asSequence()

    @Test
    fun solve1() {
        assertEquals(605, Day9.solve1(example))
        println(Day9.solve1(input))
    }

    @Test
    fun solve2() {
        assertEquals(982, Day9.solve2(example))
        println(Day9.solve2(input))
    }
}