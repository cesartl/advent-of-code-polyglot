package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day15Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2021, 15)

    val example = """1163751742
1381373672
2136511328
3694931569
7463417111
1319128137
1359912421
3125421639
1293138521
2311944581""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day15.solve1(example))
        println(Day15.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day15.solve2(example))
        println(Day15.solve2(puzzleInput))
    }
}