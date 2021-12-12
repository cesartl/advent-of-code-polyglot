package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day11Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2021, 11)

    val smallExample = """11111
19991
19191
19991
11111""".splitToSequence("\n")

    val example = """5483143223
2745854711
5264556173
6141336146
6357385478
4167524645
2176841721
6882881134
4846848554
5283751526""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day11.solve1(example))
        println(Day11.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day11.solve2(example))
        println(Day11.solve2(puzzleInput))
    }
}