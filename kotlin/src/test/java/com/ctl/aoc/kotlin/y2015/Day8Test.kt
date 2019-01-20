package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day8Test {

    val input = InputUtils.getLines(2015, 18)

    val example = InputUtils.getLines("2015", "day8-ex.txt")

    @Test
    fun countChar() {
        assertEquals(0, Day8.countChar(""""""""))
        assertEquals(3, Day8.countChar(""""abc""""))
        assertEquals(7, Day8.countChar(""""aaa\"aaa""""))
        assertEquals(2, Day8.countChar(""""\x27a""""))
        Day8.countChar(""""vvdnb\\x\\"""")
    }

    @Test
    internal fun encode() {
        assertEquals(""""\"\""""", Day8.encode(""""""""))
        assertEquals(""""\"abc\""""", Day8.encode(""""abc""""))
        assertEquals(""""\"aaa\\\"aaa\""""", Day8.encode(""""aaa\"aaa""""))
        assertEquals(""""\"\\x27\""""", Day8.encode(""""\x27""""))
    }

    @Test
    internal fun solve1() {
        assertEquals(12, Day8.solve1(example))
        println(Day8.solve1(input))
    }

    @Test
    internal fun solve2() {
        assertEquals(19, Day8.solve2(example))
        println(Day8.solve2(input))
    }
}