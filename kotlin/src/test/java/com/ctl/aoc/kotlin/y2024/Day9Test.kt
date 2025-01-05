package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day9Test {

    val puzzleInput = InputUtils.downloadAndGetSequence(2024, 9)

    val example = "2333133121414131402"

    @Test
    fun solve1() {
        println(Day9.solve1(example))
//        println(Day9.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
//        println(Day9.solve2(example))
//        println(Day9.solve2("12345"))
        //6321896265143
        println (Day9.solve2(puzzleInput))
    }
}
