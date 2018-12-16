package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day16Test{
    val input = InputUtils.getLines("2018", "day16_1.txt").toList()
    val spec1 = """Before: [3, 2, 1, 1]
9 2 1 2
After:  [3, 2, 2, 1]
"""

    @Test
    internal fun parseSpec() {
        val spec = Day16.parseSpec(spec1.split("\n").map { it.trim() })
        println(spec)

        assertEquals(3, Day16.matchForSpec(spec).size)
    }

    @Test
    internal fun testPart1() {
        println(Day16.solve1(input))
    }
}