package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day16Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2022, 16)

    val exampleInput = """Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
Valve BB has flow rate=13; tunnels lead to valves CC, AA
Valve CC has flow rate=2; tunnels lead to valves DD, BB
Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
Valve EE has flow rate=3; tunnels lead to valves FF, DD
Valve FF has flow rate=0; tunnels lead to valves EE, GG
Valve GG has flow rate=0; tunnels lead to valves FF, HH
Valve HH has flow rate=22; tunnel leads to valve GG
Valve II has flow rate=0; tunnels lead to valves AA, JJ
Valve JJ has flow rate=21; tunnel leads to valve II""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day16.solve1(exampleInput))
        println(Day16.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day16.solve2(exampleInput))
        println(Day16.solve2(puzzleInput))
    }

    @Test
    fun solve1Bis() {
        println(Day16.solve1Bis(exampleInput))
        println(Day16.solve1Bis(puzzleInput))
    }

    @Test
    fun solve2Bis() {
        println(Day16.solve2Bis(exampleInput))
        println(Day16.solve2Bis(puzzleInput))
    }

    @Test
    fun solve1Dp() {
        println(Day16.solve1Dp(exampleInput))
        println(Day16.solve1Dp(puzzleInput))
    }

    @Test
    fun solve2Dp() {
        println(Day16.solve2Dp(exampleInput))
        println(Day16.solve2Dp(puzzleInput))
    }

    @Test
    fun solve1DpFast() {
        assertEquals(1651, Day16.solve1DpFast(exampleInput))
        assertEquals(2265, Day16.solve1DpFast(puzzleInput))
    }

    @Test
    fun solve2DpFast() {
        assertEquals(1707, Day16.solve2DpFast(exampleInput))
        assertEquals(2811, Day16.solve2DpFast(puzzleInput))
    }

}
