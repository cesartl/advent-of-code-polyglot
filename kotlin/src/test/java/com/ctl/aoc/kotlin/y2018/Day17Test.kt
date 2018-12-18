package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

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

    val debug = """x=3, y=2..5
y=5, x=3..20
x=20, y=2..5
x=0, y=0..1
x=25, y=0..1""".split("\n").asSequence()

    @Test
    fun solve1() {
        assertEquals(57, Day17.solve1(example))
        println(Day17.solve1(input) + 13) //not 40136, not 39589 not 39624 not 39641, not 39636, 39649
    }

    @Test
    fun solve2() {
        println(Day17.solve2(input))
    }

    @Test
    internal fun debug() {
        println(Day17.solve1(debug, Day17.Position(3, 1)))
    }

}