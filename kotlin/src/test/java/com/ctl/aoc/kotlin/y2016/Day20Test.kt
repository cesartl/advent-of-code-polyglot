package com.ctl.aoc.kotlin.y2016

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day20Test {

    val input = InputUtils.getLines(2016, 20)

    val example = """5-8
0-2
4-7""".split("\n").asSequence()

    @Test
    fun solve1() {
        println(Day20.solve1(example)) // < 2841657
        println(Day20.solve1(input)) // < 2841657 not 50,105,778 8,327,007
    }

    @Test
    fun solve2() {
        println(Day20.solve2(input))
    }
}