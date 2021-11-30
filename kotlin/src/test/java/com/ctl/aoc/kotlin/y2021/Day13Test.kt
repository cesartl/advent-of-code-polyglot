package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day13Test {

    val puzzleInput = InputUtils.getLines(2021, 13)

    @Test
    fun solve1() {
        println(Day13.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day13.solve2(puzzleInput))
    }
}