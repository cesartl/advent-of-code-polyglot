package com.ctl.aoc.kotlin.y2015

import org.junit.jupiter.api.Test

internal class Day20Test {

    @Test
    fun presentSequence() {
        println(Day20.presentSequence().take(100).toList())
    }

    @Test
    fun solve1() {
        println(Day20.solve1(15))
    }
}