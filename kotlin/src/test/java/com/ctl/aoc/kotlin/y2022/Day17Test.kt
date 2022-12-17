package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day17Test {

    val puzzleInput = InputUtils.downloadAndGetString(2022, 17)
    val exampleInput = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"

//    @Test
//    fun solve1() {
//        println(Day17.solve1(exampleInput))
//        println(Day17.solve1(puzzleInput))
//    }

    @Test
    fun solve2() {
        println(Day17.solve2(exampleInput, 100))
        println(Day17.solve2(puzzleInput, 3000))
//        <1572093023268
    }
}
