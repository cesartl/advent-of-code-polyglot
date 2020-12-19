package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day19Test {

    val puzzleInput = InputUtils.getString(2020, 19)

    val example = """0: 4 1 5
1: 2 3 | 3 2
2: 4 4 | 5 5
3: 4 5 | 5 4
4: "a"
5: "b"

ababbb
bababa
abbbab
aaabbb
aaaabbb"""


    @Test
    fun solve1() {
        println(Day19.solve1(example))
        println(Day19.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day19.solve2(puzzleInput))
    }
}