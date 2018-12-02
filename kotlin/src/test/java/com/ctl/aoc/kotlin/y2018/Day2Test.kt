package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day2Test {

    @Test
    internal fun testPart1() {
        val s1 = sequenceOf("abcdef", "bababc", "abbcde", "abcccd", "aabcdd", "abcdee", "ababab")
        assertEquals(12, Day2p1.checksum(s1))
        val input = InputUtils.getLines("2018", "day2.txt")
        println(Day2p1.checksum(input))
    }

    @Test
    internal fun testPart2() {
        assertEquals("fgij", Day2p2.findDifference("fghij", "fguij"))
        val s1 = """
            abcde
fghij
klmno
pqrst
fguij
axcye
wvxyz"""
        val seq1 = s1.splitToSequence("\n").map { it.trim() }.filter { it != "" }
        assertEquals("fgij", Day2p2.solve(seq1))

        val input = InputUtils.getLines("2018", "day2.txt")
        println(Day2p2.solve(input))

    }
}
