package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day6Test {

    val puzzleInput = InputUtils.downloadAndGetString(2021, 6)

    val example = "3,4,3,1,2"

    @Test
    fun solve1() {
        println(Day6.solve1(example))
        println(Day6.solve1(puzzleInput))
    }

    @Test
    internal fun test() {
    }

    @Test
    fun solve2() {
        println(Day6.solve2(example))
        println(Day6.solve2(puzzleInput))
        println(Day6.solve2Bis(example))
        println(Day6.solve2Bis(puzzleInput))
    }
}