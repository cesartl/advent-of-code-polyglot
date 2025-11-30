package com.ctl.aoc.kotlin.y2025

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day22Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2025, 22)

    val example = """foo""".lineSequence()

    @Test
    fun solve1() {
        println(Day22.solve1(example))
        println(Day22.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day22.solve2(example))
        println(Day22.solve2(puzzleInput))
    }
}
