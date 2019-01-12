package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day14Test {

    val input = InputUtils.getLines(2015, 14)

    val example = "Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.\nDancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.".split("\n").asSequence()

    @Test
    fun solve1() {
        assertEquals(1120, Day14.solve1(example, 1000))
        println(Day14.solve1(input, 2503))
    }

    @Test
    fun solve2() {
        assertEquals(689, Day14.solve2(example, 1000))
        println(Day14.solve2(input, 2503))
    }
}