package com.ctl.aoc.kotlin.y2016

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day19Test{
    @Test
    internal fun highestOneBit() {
        println(Integer.highestOneBit(3))
        println(Integer.highestOneBit(4))
        println(Integer.highestOneBit(6))
        println(Integer.highestOneBit(9))
    }

    @Test
    internal fun part1() {
        assertEquals(3, Day19.josephus(5))
        println(Day19.josephus(3001330))
    }

    @Test
    internal fun lowerPoserOf() {
        assertEquals(3, Day19.lowPowerOf(3, 3))
        assertEquals(3, Day19.lowPowerOf(4, 3))
        assertEquals(3, Day19.lowPowerOf(5, 3))
        assertEquals(3, Day19.lowPowerOf(8, 3))
        assertEquals(9, Day19.lowPowerOf(9, 3))
        assertEquals(9, Day19.lowPowerOf(15, 3))
        assertEquals(27, Day19.lowPowerOf(28, 3))
    }

    @Test
    internal fun part2() {
        assertEquals(2, Day19.part2(5))
        for (i in 1..100){
            println("$i: ${Day19.part2(i)}")
            assertEquals(Day19.part2(i), Day19.part2bis(i))
        }
        println(Day19.part2bis(3001330))
    }
}