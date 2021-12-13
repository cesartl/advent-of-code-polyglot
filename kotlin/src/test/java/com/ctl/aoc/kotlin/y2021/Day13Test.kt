package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day13Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2021, 13)

    val example = """6,10
0,14
9,10
0,3
10,4
4,11
6,0
6,12
4,1
0,13
10,12
3,4
3,0
8,4
1,10
2,14
8,10
9,0

fold along y=7
fold along x=5""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day13.solve1(example))
        println(Day13.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        Day13.solve2(example)
        println()
        Day13.solve2(puzzleInput)
        println()
    }
}