package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.InputUtils
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
        val cookie = Day15.Cookie(listOf(44, 56), ingredients)
        assertEquals(62842880L, cookie.score1)
    }

    @Test
    fun solve1() {
        assertEquals(62842880L, Day15.solve1(example).score1)
        println(Day15.solve1(input).score1) // > 221760
    }
}