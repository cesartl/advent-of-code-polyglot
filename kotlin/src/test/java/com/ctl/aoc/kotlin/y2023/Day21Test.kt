package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day21Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2023, 21)

    val example = """...........
.....###.#.
.###.##..#.
..#.#...#..
....#.#....
.##..S####.
.##..#...#.
.......##..
.##.#.####.
.##..##.##.
...........""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day21.solve1(example, 6))
        println(Day21.solve1(puzzleInput, 64))
    }

    @Test
    fun solve2() {
        println(Day21.solve2(puzzleInput))
    }

    @Test
    fun solve2Bis() {
        println(Day21.solve2Bis(puzzleInput))
    }
}
