package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day19Test {

    val puzzleInput = InputUtils.getString(2019, 19).split(",").map { it.toLong() }.toLongArray()

    @Test
    fun solve1() {
        println(Day19.solve1(puzzleInput))
    }

    @Test
    internal fun explore() {
        println(Day19.exploreAndPrint(puzzleInput, 718 - 105, 947, 718 + 1, 958 + 100))
    }

    @Test
    fun solve2() {
        println(Day19.solve2(puzzleInput)) // not 7180948
    }
}