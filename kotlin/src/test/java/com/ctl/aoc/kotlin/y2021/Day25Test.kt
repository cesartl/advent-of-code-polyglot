package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day25Test {

    val puzzleInput = InputUtils.getLines(2021, 25)

    @Test
    fun solve1() {
        println(Day25.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day25.solve2(puzzleInput))
    }
}