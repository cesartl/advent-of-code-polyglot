package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day21Test {

    val input = InputUtils.getLines("2018", "day21.txt")

    @Test
    fun solve1() {
//        println(Day21.solve1()) //not 10654264 not 8343751
        assertEquals(10720163, Day21.solve1())
    }

    @Test
    internal fun solve2() {
        val l = Day21.solve2()
        println(l.last())
    }
}