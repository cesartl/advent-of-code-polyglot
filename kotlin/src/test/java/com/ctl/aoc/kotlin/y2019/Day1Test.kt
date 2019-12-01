package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day1Test {

    val input = InputUtils.getLines(2019, 1).map { it.toLong() }

    @Test
    fun part1() {
        println(Day1.part1(input))
    }

    @Test
    internal fun part2() {
        println(Day1.part2(sequenceOf(1969)))
        println(Day1.part2(sequenceOf(100756)))
        println(Day1.part2(input)) // not 4944797
    }
}