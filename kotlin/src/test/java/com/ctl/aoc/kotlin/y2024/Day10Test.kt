package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day10Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2024, 10)

    val example = """89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day10.solve1(example))
        println(Day10.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day10.solve2(example))
        println(Day10.solve2(puzzleInput))
    }
}
