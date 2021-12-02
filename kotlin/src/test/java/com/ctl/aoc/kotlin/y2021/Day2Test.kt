package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day2Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2021, 2)

    val example = """forward 5
down 5
forward 8
up 3
down 8
forward 2""".splitToSequence("\n")

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