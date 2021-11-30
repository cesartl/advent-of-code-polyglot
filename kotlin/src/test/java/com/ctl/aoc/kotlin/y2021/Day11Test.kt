package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day11Test {

    val puzzleInput = InputUtils.getLines(2021, 11)

    @Test
    fun solve1() {
        println(Day11.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day11.solve2(puzzleInput))
    }
}