package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day6Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2024, 6)

    val example = """....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#...""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day6.solve1(example))
        println(Day6.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day6.solve2(example))
        println(Day6.solve2(puzzleInput))
    }
}
