package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day7Test {

    val input = InputUtils.getLines(2015, 7)

    val example = """123 -> x
456 -> y
x AND y -> d
x OR y -> e
x LSHIFT 2 -> f
y RSHIFT 2 -> g
NOT x -> h
NOT y -> i""".split("\n").asSequence().map { Day7.Instruction.parse(it) }

    @Test
    fun resolve() {
        val state = Day7.resolve(example)
        assertEquals(72.toShort(), state.wires["d"])
        assertEquals(507.toShort(), state.wires["e"])
        assertEquals(492.toShort(), state.wires["f"])
        assertEquals(114.toShort(), state.wires["g"])
        assertEquals(65412.toShort(), state.wires["h"])
        assertEquals(65079.toShort(), state.wires["i"])
        assertEquals(123.toShort(), state.wires["x"])
        assertEquals(456.toShort(), state.wires["y"])
    }

    @Test
    internal fun solve1() {
        println(Day7.solve1(input))
    }

    @Test
    internal fun solve2() {
        println(Day7.solve2(input))
    }
}