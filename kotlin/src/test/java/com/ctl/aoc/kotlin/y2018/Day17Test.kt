package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day17Test {

    val input = InputUtils.getLines("2018", "day17.txt")

    val example = """x=495, y=2..7
y=7, x=495..501
x=501, y=3..7
x=498, y=2..4
x=506, y=1..2
x=498, y=10..13
x=504, y=10..13
y=13, x=498..504""".split("\n").asSequence()

    @Test
    fun solve1() {
//        assertEquals(57, Day17.solve1(example))
        println(Day17.solve1(input))
    }
}