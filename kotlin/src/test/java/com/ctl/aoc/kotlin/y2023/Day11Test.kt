package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day11Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2023, 11)

    val example = """...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....""".splitToSequence("\n")

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
