package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day18Test {

    val puzzleInput = InputUtils.getLines(2019, 18)

    val example1 = """########################
#f.D.E.e.C.b.A.@.a.B.c.#
######################.#
#d.....................#
########################""".splitToSequence('\n')

    @Test
    fun solve1() {
        println(Day18.solve1(example1))
        println(Day18.solve1(puzzleInput))
    }
}