package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day9Test {

    val puzzleInput = InputUtils.getLines(2020, 9)

    val example = """35
20
15
25
47
40
62
55
65
95
102
117
150
182
127
219
299
277
309
576""".splitToSequence("\n")

    @Test
    fun solve1() {
//        println(Day9.solve1(example))
        println(Day9.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        //1105310476
        println(Day9.solve2(example, 5))
        println(Day9.solve2(puzzleInput))
    }
}