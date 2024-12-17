package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day16Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2024, 16)

    val example = """###############
#.......#....E#
#.#.###.#.###.#
#.....#.#...#.#
#.###.#####.#.#
#.#.#.......#.#
#.#.#####.###.#
#...........#.#
###.#.#####.#.#
#...#.....#.#.#
#.#.#.###.#.#.#
#.....#...#.#.#
#.###.#.#.#.#.#
#S..#.....#...#
###############""".lineSequence()

    @Test
    fun solve1() {
        println(Day16.solve1(example))
        //<67404
        println(Day16.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day16.solve2(example))
        println(Day16.solve2(puzzleInput))
    }
}
