package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day4Test {

    val puzzleInput = InputUtils.getLines(2021, 4)

    @Test
    fun solve1() {
        println(Day4.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day4.solve2(puzzleInput))
    }
}