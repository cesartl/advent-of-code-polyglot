package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day21Test {

    val puzzleInput = InputUtils.getString(2019, 21)

    @Test
    fun solve1() {
        println(Day21.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day21.solve2(puzzleInput))
    }
}