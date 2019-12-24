package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day23Test {

    val puzzleInput = InputUtils.getString(2019, 23)

    @Test
    fun solve1() {
        println(Day23.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day23.solve2(puzzleInput))
    }
}