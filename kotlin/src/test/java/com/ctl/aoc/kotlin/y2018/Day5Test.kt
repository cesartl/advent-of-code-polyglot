package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day5Test {

    val input = InputUtils.getString("2018", "day5.txt")

    @Test
    fun solve1() {
        assertTrue(Day5.react('a', 'A'))
        assertTrue(Day5.react('A', 'a'))
        assertFalse(Day5.react('A', 'A'))
        assertFalse(Day5.react('x', 'A'))
//        assertEquals("dabAcCaCBAcCcaDA", Day5.reduceString("dabAcCaCBAcCcaDA"))
        assertEquals("dabCBAcaDA", Day5.solve1("dabAcCaCBAcCcaDA"))

        println(Day5.reduceString(input).length)
    }

    @Test
    internal fun testSovle2() {
        assertEquals("daDA", Day5.solve2("dabAcCaCBAcCcaDA"))
        println(Day5.solve2(input).length)
    }
}