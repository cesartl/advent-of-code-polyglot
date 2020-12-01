package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day1Test {

    val puzzleInput = InputUtils.getLines(2020, 1).map { it.toInt() }
    val example = """1721
979
366
299
675
1456""".splitToSequence("\n").map { it.toInt() }

    @Test
    fun solve1() {
        println(Day1.solve1(example))
        println(Day1.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day1.solve2(example))
        println(Day1.solve2(puzzleInput))
    }
}