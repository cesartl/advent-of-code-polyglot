package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day14Test {

    val puzzleInput = InputUtils.getLines(2020, 14)

    val exampe = """mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
mem[8] = 11
mem[7] = 101
mem[8] = 0""".splitToSequence("\n")

    val example2 = """mask = 000000000000000000000000000000X1001X
mem[42] = 100
mask = 00000000000000000000000000000000X0XX
mem[26] = 1""".splitToSequence("\n")

    @Test
    internal fun bit() {
        val mask = Day14.Instruction.parse("mask = 000000000000000000000000000000X1001X") as Day14.Instruction.Mask
        val l = Day14.generateFloatingMasks(mask.mask).toList()
        l.forEach {
            println(it.reversed().map { b -> if (b) 1 else 0 }.joinToString(""))
        }
    }

    @Test
    fun solve1() {
        println(Day14.solve1(exampe))
        println(Day14.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day14.solve2(example2))
        println(Day14.solve2(puzzleInput))
    }
}