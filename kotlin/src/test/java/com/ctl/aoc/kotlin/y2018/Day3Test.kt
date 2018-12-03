package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day3Test {

    @Test
    internal fun testParse() {
        assertEquals(Claim("123", 3, 2, 5, 4), Day3p1.parseClaim("#123 @ 3,2: 5x4"))
    }

    @Test
    internal fun testCover() {
        val c = Claim("123", 3, 2, 5, 4)
        println(Day3p1.coveredAreas(c))
    }

    @Test
    internal fun testPart1() {
        val testInput = """#1 @ 1,3: 4x4
#2 @ 3,1: 4x4
#3 @ 5,5: 2x2"""
        println(Day3p1.solve(testInput.splitToSequence("\n")))
        val lines = InputUtils.getLines("2018", "day3.txt")
        println(Day3p1.solve(lines))
    }

    @Test
    internal fun testPart2() {
        val testInput = """#1 @ 1,3: 4x4
#2 @ 3,1: 4x4
#3 @ 5,5: 2x2"""
        assertEquals("3", Day3p1.solve2(testInput.splitToSequence("\n")))
        val lines = InputUtils.getLines("2018", "day3.txt")
        println(Day3p1.solve2(lines))
    }
}