package com.ctl.aoc.kotlin.y2025

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day9Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2025, 9)

    val example = """7,1
11,1
11,7
9,7
9,5
2,5
2,3
7,3""".lineSequence()

    @Test
    fun solve1() {
        //2147429388
        println(Day9.solve1(example))
        println(Day9.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day9.solve2(example))
//        println(Day9.solve2(puzzleInput))
    }
}
