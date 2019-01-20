package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.InputUtils
import com.ctl.aoc.kotlin.y2015.Day15.Cookie
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day15Test {

    val input = InputUtils.getLines(2015, 15)

    val example = """Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3""".split("\n").asSequence()


    @Test
    internal fun testScore() {
        val ingredients = example.map { Day15.Ingredient.parse(it) }.toList()
        println(ingredients)
        val cookie = Cookie(listOf(44, 56), ingredients)
        assertEquals(62842880L, cookie.score1)

        val c2 = Cookie(listOf(40, 60), ingredients)
        assertEquals(500, c2.calories)
    }

    @Test
    fun solve1() {
        assertEquals(62842880L, Day15.solve1(example).score1)
        println(Day15.solve1(input).score1) // > 221760
    }

    @Test
    internal fun generateALl() {
        println(Day15.generateAll(3, 3).toList().joinToString("\n"))
    }

    @Test
    fun solve2() {
        assertEquals(57600000L, Day15.solve2(example).score1)
        println(Day15.solve2(input).score1) // > 55296
    }
}