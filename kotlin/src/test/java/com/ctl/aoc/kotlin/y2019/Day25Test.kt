package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day25Test {

    val puzzleInput = InputUtils.getString(2019, 25).split(',').map { it.toLong() }
            .toLongArray()

    @Test
    fun solve1() {
        Day25.manual(puzzleInput)
    }
}