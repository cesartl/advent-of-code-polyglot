package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day24Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2022, 24)

    val example = """#.######
#>>.<^<#
#.<..<<#
#>v.><>#
#<^v^^>#
######.#""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day24.solve1(example))
        println(Day24.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day24.solve2(example))
        println(Day24.solve2(puzzleInput))
    }
}
