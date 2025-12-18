package com.ctl.aoc.kotlin.y2025

import com.ctl.aoc.kotlin.utils.InputUtils
import com.ctl.aoc.kotlin.utils.timed
import com.ctl.aoc.kotlin.utils.timedMs
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day10Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2025, 10)

    val example = """[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}""".lineSequence()

    @Test
    fun solve1() {
        println(Day10.solve1(example))
        println(timedMs { Day10.solve1(puzzleInput) })
        println(timedMs { Day10.solve1Bis(puzzleInput) })
    }

    @Test
    fun solve2() {
        println(Day10.solve2(example))
        println(timedMs { Day10.solve2(puzzleInput) })
    }
}
