package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day6Test {

    val puzzleInput = InputUtils.getLines(2021, 6)

    @Test
    fun solve1() {
        println(Day6.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day6.solve2(puzzleInput))
    }
}