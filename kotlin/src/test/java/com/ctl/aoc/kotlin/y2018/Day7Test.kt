package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day7Test {
    val example = """Step C must be finished before step A can begin.
Step C must be finished before step F can begin.
Step A must be finished before step B can begin.
Step A must be finished before step D can begin.
Step B must be finished before step E can begin.
Step D must be finished before step E can begin.
Step F must be finished before step E can begin.""".split("\n").asSequence()

    val input = InputUtils.getLines("2018", "day7.txt")

    @Test
    internal fun testPart1() {
        assertEquals("CABDFE", Day7.solve1(example))
        println(Day7.solve1(input))
    }

    @Test
    internal fun solve2() {
        assertEquals(15, Day7.solve2(example, 2) { node -> node.first() - 'A' + 1 })
        println(Day7.solve2(input, 5) { node -> 60 + (node.first() - 'A' + 1) }) // 973
    }
}