package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day6Test {

    val input = InputUtils.getLines(2015, 6)

    @Test
    fun solve1() {
        println(Day6.solve1(input))
    }

    @Test
    fun solve2() {
        println(Day6.solve2(input)) // > 75,290 > 494,432
    }
}