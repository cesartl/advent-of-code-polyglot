package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day5Test {

    val puzzleInput = InputUtils.getLines(2020, 5)

    @Test
    fun toInt() {
        println(Day5.toInt("FBFBBFF", 'B'))
        println(Day5.toInt("RLR", 'R'))
    }

    @Test
    fun seat() {
        assertEquals(567, Day5.Seat.parse("BFFFBBFRRR").id)
    }

    @Test
    fun solve1() {
        println(Day5.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day5.solve2(puzzleInput))
    }
}