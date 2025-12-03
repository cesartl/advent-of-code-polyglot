package com.ctl.aoc.kotlin.y2025

import com.ctl.aoc.kotlin.utils.InputUtils
import com.ctl.aoc.kotlin.utils.timedMs
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day3Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2025, 3)

    val example = """987654321111111
811111111111119
234234234234278
818181911112111""".lineSequence()

    @Test
    fun solve1() {
        println(Day3.solve1(example))
        println(Day3.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day3.solve2(example))
        println(timedMs { Day3.solve2(puzzleInput) })
    }
}
