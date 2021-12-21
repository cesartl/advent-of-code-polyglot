package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day21Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2021, 21)

    val example = """Player 1 starting position: 4
Player 2 starting position: 8""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day21.solve1(example))
        println(Day21.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day21.solve2(example))
        println(Day21.solve2(puzzleInput))
    }
}