package com.ctl.aoc.kotlin.y2020

import org.junit.jupiter.api.Test

internal class Day23Test {

    val puzzleInput = "643719258"
    val example = "389125467"


    @Test
    fun solve1() {
        println(Day23.solve1(example, 10))
        println(Day23.solve1(example, 100))
        println(Day23.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day23.solve2(puzzleInput))
    }
}