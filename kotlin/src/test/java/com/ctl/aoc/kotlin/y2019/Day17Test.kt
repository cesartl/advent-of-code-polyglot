package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day17Test {

    val puzzleInput = InputUtils.getString(2019, 17).split(",").map { it.toLong() }.toLongArray()

    @Test
    fun solve1() {
        println(Day17.solve1(puzzleInput))
    }

    @Test
    internal fun name() {
        println(Day17.Routine.parse("R8,R8", 'A').toAscii())
        println(Day17.Routine.parse("R12", 'A').toAscii())
    }

    @Test
    fun solve2() {
        println(Day17.solve2(puzzleInput))
    }
}