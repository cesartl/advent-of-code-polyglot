package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day15Test {

    val input = InputUtils.getLines("2018", "day15.txt").toList()
    val outcomeExample = InputUtils.getLines("2018", "day15_outcome.txt").toList()
    val example0 = InputUtils.getLines("2018", "day15_0.txt").toList()
    val example1 = InputUtils.getLines("2018", "day15_1.txt").toList()
    val example2 = InputUtils.getLines("2018", "day15_2.txt").toList()
    val example3 = InputUtils.getLines("2018", "day15_3.txt").toList()
    val example4 = InputUtils.getLines("2018", "day15_4.txt").toList()
    val example5 = InputUtils.getLines("2018", "day15_5.txt").toList()

    @Test
    internal fun testPart1() {
//        assertEquals(27730, Day15.solve1(outcomeExample))
        assertEquals(36334, Day15.solve1(example1))
        assertEquals(39514, Day15.solve1(example2))
        assertEquals(27755, Day15.solve1(example3))
        assertEquals(28944, Day15.solve1(example4))
        assertEquals(18740, Day15.solve1(example5))

        println(Day15.solve1(input))
    }
}