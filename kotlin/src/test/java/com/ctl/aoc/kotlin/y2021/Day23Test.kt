package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day23Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2021, 23)
    val example = """#############
#...........#
###B#C#B#D###
  #A#D#C#A#
  #########""".splitToSequence("\n")

    @Test
    fun solve1() {
//        assertEquals(12521, Day23.solve1(example))
        assertEquals(15111, Day23.solve1(puzzleInput))
    }

    @Test
    fun solve1Bis() {
//        assertEquals(12521, Day23.solve1Bis(example))
        assertEquals(15111, Day23.solve1Bis(puzzleInput))
    }

    @Test
    fun solve2() {
//        println(Day23.solve2(example))
        assertEquals(47625, Day23.solve2(puzzleInput))
    }

    @Test
    fun solve2Bis() {
//        assertEquals(44169, Day23.solve2Bis(example))
        assertEquals(47625, Day23.solve2Bis(puzzleInput))
    }
}