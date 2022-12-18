package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day18Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2022, 18)
    val exampleInput = """2,2,2
1,2,2
3,2,2
2,1,2
2,3,2
2,2,1
2,2,3
2,2,4
2,2,6
1,2,5
3,2,5
2,1,5
2,3,5""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day18.solve1(exampleInput))
        println(Day18.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day18.solve2(exampleInput))
        println(Day18.solve2(puzzleInput))
    }
}
