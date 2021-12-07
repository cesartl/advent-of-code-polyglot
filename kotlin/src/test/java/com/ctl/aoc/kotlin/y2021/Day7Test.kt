package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day7Test {

    val puzzleInput = InputUtils.downloadAndGetString(2021, 7)

    val example = """16,1,2,0,4,2,7,1,2,14"""

    @Test
    fun solve1() {
        listOf(1).sorted()
        1 + 2
        println(Day7.solve1(example))
        println(Day7.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day7.solve2(example))
        println(Day7.solve2(puzzleInput))

        //cesar 1638853831 1638854364
        //      1638853958 1638854045
    }
}