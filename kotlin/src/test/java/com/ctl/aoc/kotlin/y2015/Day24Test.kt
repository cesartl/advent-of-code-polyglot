package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day24Test {

    private val packages = InputUtils.getLines("2015", "day24.txt").map { it.toLong() }.toList()


    @Test
    internal fun example1() {
        val ex1 = listOf(1, 2, 3, 4, 5, 7, 8, 9, 10, 11).map { it.toLong() }
        println(Day24.solve1(ex1))
        println(Day24.solve2(ex1))
    }

    @Test
    fun solve1() {
        println(Day24.solve1(packages))
    }

    @Test
    fun solve2() {
        println(Day24.solve2(packages))
    }
}