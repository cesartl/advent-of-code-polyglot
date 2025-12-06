package com.ctl.aoc.kotlin.y2025

import com.ctl.aoc.kotlin.utils.InputUtils
import com.ctl.aoc.kotlin.utils.timedMs
import org.junit.jupiter.api.Test

internal class Day5Test {

    val puzzleInput = InputUtils.downloadAndGetString(2025, 5)

    val example = """3-5
10-14
16-20
12-18

1
5
8
11
17
32"""

    @Test
    fun solve1() {
        println(Day5.solve1(example))
        println(timedMs { Day5.solve1(puzzleInput) })
    }

    @Test
    fun solve2() {
        println(Day5.solve2(example))
        println(timedMs { Day5.solve2(puzzleInput) })
    }
}
