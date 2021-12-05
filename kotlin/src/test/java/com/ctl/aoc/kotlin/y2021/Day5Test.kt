package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day5Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2021, 5)

    val example = """0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2""".splitToSequence("\n")

    @Test
    fun testLine() {
       val line = Day5.Line.parse("1,1 -> 1,3")
        println(line.points().toList())
    }

    @Test
    fun solve1() {
        println(Day5.solve1(example))
        println(Day5.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day5.solve2(example))
        println(Day5.solve2(puzzleInput))
    }
}