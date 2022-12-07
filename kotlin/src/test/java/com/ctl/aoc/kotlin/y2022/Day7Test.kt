package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day7Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2022, 7)

    val testInput = InputUtils.getLines("2022", "day7-example.txt")

    @Test
    fun solve1() {
        println(Day7.solve1(testInput))
        println(Day7.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day7.solve2(testInput))
        println(Day7.solve2(puzzleInput))
    }
}
