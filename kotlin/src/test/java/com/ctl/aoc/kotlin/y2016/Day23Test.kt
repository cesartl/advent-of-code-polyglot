package com.ctl.aoc.kotlin.y2016

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day23Test {

    val day12Input = InputUtils.getLines(2016, 12)

    @Test
    fun day12Solve1() {
        assertEquals(318009, Day23.day12Solve1(day12Input))
        assertEquals(9227663, Day23.day12Solve2(day12Input))
    }
}