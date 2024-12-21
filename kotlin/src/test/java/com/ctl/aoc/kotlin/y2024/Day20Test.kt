package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day20Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2024, 20)

    val example = """###############
#...#...#.....#
#.#.#.#.#.###.#
#S#...#.#.#...#
#######.#.#.###
#######.#.#...#
#######.#.###.#
###..E#...#...#
###.#######.###
#...###...#...#
#.#####.#.###.#
#.#...#.#.#...#
#.#.#.#.#.#.###
#...#...#...###
###############""".lineSequence()

    @Test
    fun solve1() {
        println(Day20.solve1(example))
        //>1498
        println(Day20.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
//        println(Day20.solve2(example))
        //>171771
        //>171870
        //>906433
        //906410
        println(Day20.solve2(puzzleInput))
    }
}
