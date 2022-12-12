package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day12Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2022, 12)

    val exampleInput = """Sabqponm
abcryxxl
accszExk
acctuvwj
abdefghi""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day12.solve1(exampleInput))
        println(Day12.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day12.solve2(exampleInput))
        println(Day12.solve2(puzzleInput))
    }
}
