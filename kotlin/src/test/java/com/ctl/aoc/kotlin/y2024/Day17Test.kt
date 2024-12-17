package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day17Test {

    val puzzleInput = InputUtils.downloadAndGetString(2024, 17)

    val example = """Register A: 729
Register B: 0
Register C: 0

Program: 0,1,5,4,3,0"""

    @Test
    fun solve1() {
        println(Day17.solve1(example))
        println(Day17.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day17.solve2(puzzleInput))
    }
}
