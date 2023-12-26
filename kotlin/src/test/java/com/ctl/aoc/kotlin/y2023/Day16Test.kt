package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day16Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2023, 16)

    val example = """.|...\....
|.-.\.....
.....|-...
........|.
..........
.........\
..../.\\..
.-.-/..|..
.|....-|.\
..//.|....""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day16.solve1(example))
        println(Day16.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day16.solve2(example))
        println(Day16.solve2(puzzleInput))
    }
}
