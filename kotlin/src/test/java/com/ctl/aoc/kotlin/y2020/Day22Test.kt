package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day22Test {

    val puzzleInput = InputUtils.getString(2020, 22)
    val example = """Player 1:
9
2
6
3
1

Player 2:
5
8
4
7
10"""

    @Test
    fun solve1() {
        println(Day22.solve1(example))
        println(Day22.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        Day22.solve2(puzzleInput)
    }
}