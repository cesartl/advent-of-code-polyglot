package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day8Test {

    val input = InputUtils.getString(2019, 8)

    @Test
    fun solve1() {
        println(Day8.solve1("123456789012", 3, 2))
        println(Day8.solve1(input, 25, 6)) //not 1540
    }

    @Test
    internal fun solve2() {
        Day8.solve2("0222112222120000", 2, 2)
        Day8.solve2(input, 25, 6)
    }
}