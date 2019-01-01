package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day5Test {

    val input = InputUtils.getLines(2015, 5)

    @Test
    fun solve1() {
        println(Day5.solve1(input))
    }

    @Test
    fun solve2() {
        println(Day5.solve2(input))
    }
}