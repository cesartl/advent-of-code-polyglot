package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day14Test {

    val puzzleInput = InputUtils.getLines(2020, 14)

    val exampe = """mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
mem[8] = 11
mem[7] = 101
mem[8] = 0""".splitToSequence("\n")

    @Test
    internal fun bit() {
        val i = Day14.Instruction.parse("mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X")
        println(i)
    }

    @Test
    fun solve1() {
        println(Day14.solve1(exampe))
        println(Day14.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        Day14.solve2(puzzleInput)
    }
}