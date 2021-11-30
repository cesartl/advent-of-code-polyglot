package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day7Test {

    val puzzleInput = InputUtils.getLines(2021, 7)

    @Test
    fun solve1() {
        println(Day7.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day7.solve2(puzzleInput))
    }
}