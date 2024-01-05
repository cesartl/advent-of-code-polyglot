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
    fun shift() {
        val memory: MutableList<Pulse> = mutableListOf()
        memory.add(true)
        memory.add(true)
        memory.add(false)
        memory.add(true)

        val n = memory.foldIndexed(0) { i, acc, p ->
            val next = if (p) {
                1.shl(memory.size - i)
            } else 0
            acc or next
        }
        println(n.toString(2))
    }

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
