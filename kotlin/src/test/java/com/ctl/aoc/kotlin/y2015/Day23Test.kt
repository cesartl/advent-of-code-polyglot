package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day23Test {

    private val instructions = InputUtils.getLines("2015", "day23.txt").map { Day23.Instruction.parse(it) }.toList()

    @Test
    internal fun solve1() {
        val t1 = System.currentTimeMillis()
        println(Day23.solve1(instructions))
        val t2 = System.currentTimeMillis()
        println(Day23.solve1Compiled())
        val t3 = System.currentTimeMillis()
        println("Part 1 ${t2 - t1}ms")
        println("Part 2 ${t2 - t2}ms")
    }

    @Test
    internal fun solve2() {
//        Day23.solve2(instructions)
        println(Day23.solve2Compiled())
    }
}