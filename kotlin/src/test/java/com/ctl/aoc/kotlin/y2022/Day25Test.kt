package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.InputUtils
import com.ctl.aoc.kotlin.y2022.Day25.plus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day25Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2022, 25)

    val example = """1=-0-2
12111
2=0=
21
2=01
111
20012
112
1=-1=
1-12
12
1=
122""".splitToSequence("\n")

    @Test
    internal fun plus() {
        assertEquals("1=".toSnafu(), "1".toSnafu() + "2".toSnafu())
    }

    @Test
    fun solve1() {
        println(Day25.solve1(example))
        println(Day25.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day25.solve2(puzzleInput))
    }
}
