package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

class Day1Test {

    val puzzleInput = InputUtils.downloadAndGetAllLines(2022, 1)

    @Test
    fun solve1() {
        println(Day1.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day1.solve2(puzzleInput))
    }
}
