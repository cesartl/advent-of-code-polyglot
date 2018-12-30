package com.ctl.aoc.kotlin.y2016

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day23Test {

    val day12Input = InputUtils.getLines(2016, 12)
    val day23Input = InputUtils.getLines(2016, 23)

    val example = """cpy 2 a
tgl a
tgl a
tgl a
cpy 1 a
dec a
dec a""".split("\n").asSequence()

    @Test
    fun day12Solve1() {
        assertEquals(318009, Day23.day12Solve1(day12Input))
        assertEquals(9227663, Day23.day12Solve2(day12Input))
    }

    @Test
    internal fun day23Sove1() {
        val exampleState = Day23.run(Day23.parse(example))
        assertEquals(3, exampleState.getValue(Day23.Ref.Register('a')))
        assertEquals(12775, Day23.solve1(day23Input))
    }

    @Test
    internal fun day23Sove2() {
        assertEquals(479009335, Day23.solve2(day23Input))
    }
}