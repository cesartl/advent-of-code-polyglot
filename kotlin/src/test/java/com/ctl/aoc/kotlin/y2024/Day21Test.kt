package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day21Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2024, 21)

    val example = """029A
980A
179A
456A
379A""".lineSequence()

    @Test
    fun solve1() {
        println(Day21.solve1(example))
        //<161468
        println(Day21.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day21.solve2(puzzleInput))
    }
}
