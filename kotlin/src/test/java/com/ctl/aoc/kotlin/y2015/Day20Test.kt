package com.ctl.aoc.kotlin.y2015

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day20Test {


    @Test
    fun solve1() {
        assertEquals(6, Day20.solve1(120))
        assertEquals(8, Day20.solve1(150))
        println(Day20.solve1(34000000))
    }

    @Test
    internal fun solve2() {
        println(Day20.solve2(34000000))
    }
}