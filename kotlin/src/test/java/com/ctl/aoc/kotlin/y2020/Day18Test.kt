package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day18Test {

    val puzzleInput = InputUtils.getLines(2020, 18)


    @Test
    internal fun eval() {
//        assertEquals(71, Day18.evaluateExpression("1 + 2 * 3 + 4 * 5 + 6"))
//        assertEquals(51, Day18.evaluateExpression("1 + (2 * 3) + (4 * (5 + 6))"))
//        println(Day18.evaluateExpression("(2+4*9)*(6+9*8+6)+6"))
        assertEquals(13632, Day18.evaluateExpression("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))
    }

    @Test
    internal fun findBracket() {
        val s = "(4 * (5 + 6))"
        println(Day18.findClosingBracket(s, 5))
    }

    @Test
    fun solve1() {
        println(Day18.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day18.solve2(puzzleInput))
    }
}