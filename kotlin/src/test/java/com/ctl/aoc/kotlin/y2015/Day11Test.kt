package com.ctl.aoc.kotlin.y2015

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day11Test {

    @Test
    fun increment() {
        assertEquals("b", Day11.increment("a"))
        assertEquals("xy", Day11.increment("xx"))
        assertEquals("xz", Day11.increment("xy"))
        assertEquals("ya", Day11.increment("xz"))
        assertEquals("aaaaaa", Day11.increment("zzzzzz"))
    }

    @Test
    internal fun validPassword() {
        assertFalse(Day11.isPasswordValid("hijklmmn"))
        assertFalse(Day11.isPasswordValid("abbceffg"))
        assertFalse(Day11.isPasswordValid("abbcegjk"))
        assertTrue(Day11.isPasswordValid("abcdffaa"))
    }

    @Test
    internal fun solve1() {
        assertEquals("abcdffaa", Day11.solve1("abcdefgh"))
        assertEquals("ghjaabcc", Day11.solve1("ghijklmn"))
        println(Day11.solve1("vzbxkghb"))
    }

    @Test
    internal fun solve2() {
        println(Day11.solve2("vzbxkghb"))
    }
}