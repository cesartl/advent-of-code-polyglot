package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.Position
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day22Test {

    val inputDepth = 11541
    val inputTarget = Position(14, 778)

    @Test
    fun solve1() {
//        assertEquals(114, Day22.solve1(510, Position(10, 10)))
        println(Day22.solve1(inputDepth, inputTarget)) //not 11669
    }

    @Test
    internal fun solve2() {
        assertEquals(45, Day22.solve2(510, Position(10, 10)))
    }
}