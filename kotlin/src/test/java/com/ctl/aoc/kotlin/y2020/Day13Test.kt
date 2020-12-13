package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day13Test {

    val puzzleInput = InputUtils.getLines(2020, 13)
    val example = """939
7,13,x,x,59,x,31,19""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day13.solve1(example))
        println(Day13.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day13.solve2(example))
        println(Day13.solve2(puzzleInput))
    }
}