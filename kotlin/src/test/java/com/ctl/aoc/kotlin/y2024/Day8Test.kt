package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day8Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2024, 8)

    val example = """............
........0...
.....0......
.......0....
....0.......
......A.....
............
............
........A...
.........A..
............
............""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day8.solve1(example))
        println(Day8.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day8.solve2(example))
        println(Day8.solve2(puzzleInput))
    }
}
