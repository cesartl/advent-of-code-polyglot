package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day14Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2023, 14)

    val example = """O....#....
O.OO#....#
.....##...
OO.#O....O
.O.....O#.
O.#..O.#.#
..O..#O..O
.......O..
#....###..
#OO..#....""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day14.solve1(example))
        println(Day14.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day14.solve2(example))
        println(Day14.solve2(puzzleInput))
    }
}
