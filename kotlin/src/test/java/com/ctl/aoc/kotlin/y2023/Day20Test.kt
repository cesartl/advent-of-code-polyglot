package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day20Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2023, 20)

    val example1 = """broadcaster -> a, b, c
%a -> b
%b -> c
%c -> inv
&inv -> a""".splitToSequence("\n")

    val example2 = """broadcaster -> a
%a -> inv, con
&inv -> b
%b -> con
&con -> output
output ->
""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day20.solve1(example1))
        println(Day20.solve1(example2))
        println(Day20.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day20.solve2(puzzleInput))
    }
}
