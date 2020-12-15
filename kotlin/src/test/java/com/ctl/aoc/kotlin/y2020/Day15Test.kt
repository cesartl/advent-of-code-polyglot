package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.InputUtils
import com.ctl.aoc.kotlin.utils.timedMs
import org.junit.jupiter.api.Test

internal class Day15Test {

    val puzzleInput = InputUtils.getString(2020, 15)

    val example = "0,3,6"

    @Test
    fun solve1() {
        println(Day15.solve1(example))
        println(Day15.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(timedMs { Day15.solve2(example) })
        println(timedMs { Day15.solve2(puzzleInput) })
    }
}