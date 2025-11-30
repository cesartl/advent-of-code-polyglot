package com.ctl.aoc.kotlin.y2025

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day5Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2025, 5)

    val example = """foo""".lineSequence()

    @Test
    fun solve1() {
        println(Day5.solve1(example))
        println(Day5.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day5.solve2(example))
        println(Day5.solve2(puzzleInput))
    }
}
