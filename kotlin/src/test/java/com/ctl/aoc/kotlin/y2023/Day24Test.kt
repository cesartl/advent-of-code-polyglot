package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day24Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2023, 24)

    val example = """19, 13, 30 @ -2,  1, -2
18, 19, 22 @ -1, -1, -2
20, 25, 34 @ -2, -2, -4
12, 31, 28 @ -1, -2, -1
20, 19, 15 @  1, -5, -3""".splitToSequence("\n")

    @Test
    fun solve1() {
        val smallRange = 7L..27L
        println(Day24.solve1(example, smallRange, smallRange))
        val bigRange = 200000000000000L..400000000000000L
        println(Day24.solve1(puzzleInput, bigRange, bigRange))
        //<32334
    }

    @Test
    fun solve2() {
//        println(Day24.solve2(example))
        println(Day24.solve2(puzzleInput))
    }
}
