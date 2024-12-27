package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day22Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2024, 22)

    val example = """1
10
100
2024""".lineSequence()

    val example2 = """1
2
3
2024""".lineSequence()

    @Test
    fun solve1() {
        println(Day22.solve1(example))
        println(Day22.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
//        println(Day22.solve2(example2))
        println(Day22.solve2(puzzleInput))
    }
}
