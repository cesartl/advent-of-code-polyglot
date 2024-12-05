package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day4Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2024, 4)

    val example = """MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day4.solve1(example))
        println(Day4.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day4.solve2(example))
        println(Day4.solve2(puzzleInput))
    }
}
