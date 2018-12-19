package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day19Test {

    val input = InputUtils.getLines("2018", "day19.txt").toList()
    val example = """#ip 0
seti 5 0 1
seti 6 0 2
addi 0 1 0
addr 1 2 3
setr 1 0 0
seti 8 0 4
seti 9 0 5""".split("\n")

    @Test
    fun solve1() {
//        assertEquals(7, Day19.solve1(example))
        assertEquals(2106, Day19.solve1(input))
    }

    @Test
    internal fun solve2() {
        println(Day19.solve2(input)) //not 4
    }
}