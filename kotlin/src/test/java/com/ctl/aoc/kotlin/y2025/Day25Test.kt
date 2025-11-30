package com.ctl.aoc.kotlin.y2025

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day25Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2025, 25)

    val example = """foo""".lineSequence()

    @Test
    fun solve1() {
        println(Day25.solve1(example))
        println(Day25.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day25.solve2(example))
        println(Day25.solve2(puzzleInput))
    }
}
