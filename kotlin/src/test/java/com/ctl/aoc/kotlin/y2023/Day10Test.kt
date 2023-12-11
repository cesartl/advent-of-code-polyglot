package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day10Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2023, 10)

    val ex1 = """-L|F7
7S-7|
L|7||
-L-J|
L|-JF""".splitToSequence("\n")

    val ex2 = """..F7.
.FJ|.
SJ.L7
|F--J
LJ...""".splitToSequence("\n")

    @Test
    fun solve1() {
//        println(Day10.solve1(ex1))
//        println(Day10.solve1(ex2))
        println(Day10.solve1(puzzleInput))
        //> 308
    }

    @Test
    fun solve2() {
        println(Day10.solve2(puzzleInput))
    }
}
