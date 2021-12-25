package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
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
        println(Day23.solve1(example))
        println(Day23.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
//        println(Day23.solve2(example))
        println(Day23.solve2(puzzleInput))
    }
}