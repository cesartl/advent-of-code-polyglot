package com.ctl.aoc.kotlin.y2025

import com.ctl.aoc.kotlin.utils.InputUtils
import com.ctl.aoc.kotlin.utils.timed
import com.ctl.aoc.kotlin.utils.timedMs
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day1Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2025, 1)

    val example = """L68
L30
R48
L5
R60
L55
L1
L99
R14
L82""".lineSequence()

    @Test
    fun solve1() {
        println(Day1.solve1(example))
        println(Day1.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(timedMs { Day1.solve2(example) })
        println(timedMs { Day1.solve2(puzzleInput) })

        println(timedMs { Day1.solve2Bis(example) })
        println(timedMs { Day1.solve2Bis(puzzleInput) })
    }
}
