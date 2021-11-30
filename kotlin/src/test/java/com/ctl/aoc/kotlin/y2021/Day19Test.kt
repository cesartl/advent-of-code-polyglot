package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day19Test {

    val puzzleInput = InputUtils.getLines(2021, 19)

    @Test
    fun solve1() {
        println(Day19.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day19.solve2(puzzleInput))
    }
}