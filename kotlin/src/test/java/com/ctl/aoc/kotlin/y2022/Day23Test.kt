package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day23Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2022, 23)

    val example = """....#..
..###.#
#...#.#
.#...##
#.###..
##.#.##
.#..#..""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day23.solve1(example))
        println(Day23.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day23.solve2(example))
        println(Day23.solve2(puzzleInput))
    }
}
