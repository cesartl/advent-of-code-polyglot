package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day6Test {

    val puzzleInput = InputUtils.downloadAndGetString(2022, 6)

    @Test
    fun solve1() {
        assertEquals(7, Day6.solve1("mjqjpqmgbljsphdztnvjfqwrcgsmlb"))
        println(Day6.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        assertEquals(19, Day6.solve2("mjqjpqmgbljsphdztnvjfqwrcgsmlb"))
        println(Day6.solve2(puzzleInput))
    }
}
