package com.ctl.aoc.kotlin.y2025

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day2Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2025, 2)

    val example = """foo""".lineSequence()

    @Test
    fun solve1() {
        println(Day2.solve1(example))
        println(Day2.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day2.solve2(example))
        println(Day2.solve2(puzzleInput))
    }
}
