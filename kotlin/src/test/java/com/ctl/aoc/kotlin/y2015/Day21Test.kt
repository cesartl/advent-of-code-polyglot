package com.ctl.aoc.kotlin.y2015

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class Day21Test {

    @Test
    fun playerWins() {
        val player = Day21.Character(5, 5, 8)
        val boss = Day21.Character(7, 2, 12)
        assertThat(Day21.playerWins(player, boss)).isTrue()
    }

    @Test
    internal fun solve1() {
        println(Day21.solve1(Day21.Character(8, 1, 104)))
    }

    @Test
    internal fun solve2() {
        println(Day21.solve2(Day21.Character(8, 1, 104)))
    }
}