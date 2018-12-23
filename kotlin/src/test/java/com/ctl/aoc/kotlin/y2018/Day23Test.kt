package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day23Test {

    val example1 = """pos=<0,0,0>, r=4
pos=<1,0,0>, r=1
pos=<4,0,0>, r=3
pos=<0,2,0>, r=1
pos=<0,5,0>, r=3
pos=<0,0,3>, r=1
pos=<1,1,1>, r=1
pos=<1,1,2>, r=1
pos=<1,3,1>, r=1""".split("\n").asSequence()

    val example2 = """pos=<10,12,12>, r=2
pos=<12,14,12>, r=2
pos=<16,12,12>, r=4
pos=<14,14,14>, r=6
pos=<50,50,50>, r=200
pos=<10,10,10>, r=5""".split("\n").asSequence()

    val input = InputUtils.getLines("2018", "day23.txt")

    @Test
    fun solve1() {
        assertEquals(7, Day23.solve1(example1))
        println(Day23.solve1(input))
    }

    @Test
    fun solve2() {
//        assertEquals(36,Day23.solve2(example2))
        println(Day23.solve2(input)) // > 55919980 < 255198677
    }
}