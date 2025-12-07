package com.ctl.aoc.kotlin.y2025

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day6Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2025, 6)

    val example = """123 328  51 64 
 45 64  387 23 
  6 98  215 314
*   +   *   + """.lineSequence()

    @Test
    fun solve1() {
        println(Day6.solve1(example))
        println(Day6.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day6.solve2(example))
        println(Day6.solve2(puzzleInput))
    }
}
