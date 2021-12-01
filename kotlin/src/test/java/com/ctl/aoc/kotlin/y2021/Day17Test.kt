package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day17Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2021, 17)

    @Test
    fun solve1() {
        println(Day17.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day17.solve2(puzzleInput))
    }
}