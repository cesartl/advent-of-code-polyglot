package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day3Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2023, 3)

    val example = """467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day3.solve1(example))
        println(Day3.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day3.solve2(example))
        println(Day3.solve2(puzzleInput))
    }
}
