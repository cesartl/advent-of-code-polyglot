package com.ctl.aoc.kotlin.y2015

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day10Test {

    @Test
    fun nextCountAndSay() {
        assertEquals("11", Day10.nextCountAndSay("1"))
        assertEquals("21", Day10.nextCountAndSay("11"))
        assertEquals("1211", Day10.nextCountAndSay("21"))
        assertEquals("111221", Day10.nextCountAndSay("1211"))
    }

    @Test
    internal fun solve1() {
        println(Day10.solve1("1113122113"))
    }

    @Test
    internal fun solve2() {
        println(Day10.solve2("1113122113"))
    }
}