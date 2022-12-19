package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day19Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2022, 19)

    val exampleInput = """Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day19.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day19.solve2(puzzleInput))
    }
}
