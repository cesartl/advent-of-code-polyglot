package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day6Test {

    val puzzleInput = InputUtils.getLines(2020, 6)

    val example = """abc

a
b
c

ab
ac

a
a
a
a

b""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day6.solve1(example))
        println(Day6.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day6.solve2(puzzleInput))
    }
}