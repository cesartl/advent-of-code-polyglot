package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day16Test {

    val puzzleInput = InputUtils.getString(2020, 16)

    val example = """class: 0-1 or 4-19
row: 0-5 or 8-19
seat: 0-13 or 16-19

your ticket:
11,12,13

nearby tickets:
3,9,18
15,1,5
5,14,9"""

    @Test
    fun solve1() {
        println(Day16.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
//        println(Day16.solve2(example))
        println(Day16.solve2(puzzleInput))
    }
}