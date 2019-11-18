package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.y2015.Day22.Spell.ActionSpell.*
import com.ctl.aoc.kotlin.y2015.Day22.Spell.EffectSpell.*
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day22Test {

    @Test
    internal fun testSimpleBattle() {
        val player = Day22.Character(0, 0, 10, 250)
        val boss = Day22.Character(8, 0, 13, 0)

        val s1 = Day22.GameState(player, boss, listOf())

        val s2 = s1.nextState(Day22.Spell.EffectSpell.Poison)

        val s3 = s2.nextState(Day22.Spell.ActionSpell.MagicMissile)

        println(s3)
    }

    @Test
    internal fun testSimpleBattle2() {
        val boss = Day22.Character.boss(14, 8)
        val player = Day22.Character.player(10, 250)
        val spellsToCast = listOf(Recharge, Shield, Drain, Poison, MagicMissile)

        var s = Day22.GameState(player, boss)
        spellsToCast.forEach { spell ->
            assertThat(s.canPlayerCastSpell(spell)).isTrue()
            s = s.nextState(spell)
            println("State is $s")
            println("")
        }
        assertThat(s.isFinished()).isTrue()
        assertThat(s.boss.isAlive()).isFalse()

        var manas = mutableListOf<Int>()
        var c: Day22.GameState? = s
        while (c != null) {
            manas.add(c.player.mana)
            c = c.previous
        }
        println(manas)
    }

    @Test
    internal fun debug1() {
        val spells = listOf(Recharge, Shield, Poison, Recharge, MagicMissile, Shield, Recharge, Poison, MagicMissile, Shield, Poison, MagicMissile, MagicMissile, MagicMissile)
        val boss = Day22.Character.boss(71, 10)
        val player = Day22.Character.player(50, 500)
        trySpells(spells, player, boss)
    }

    fun trySpells(spellsToCast: List<Day22.Spell>, player: Day22.Character, boss: Day22.Character) {
        var s = Day22.GameState(player, boss)
        spellsToCast.forEach { spell ->
            assertThat(s.canPlayerCastSpell(spell)).isTrue()
            s = s.nextState(spell)
//            println("State is $s")
            assertThat(s.player.isAlive()).isTrue()
        }
        assertThat(s.isFinished()).isTrue()
        assertThat(s.boss.isAlive()).isFalse()

        var manas = mutableListOf<Int>()
        var c: Day22.GameState? = s
        while (c != null) {
            manas.add(c.player.mana)
            c = c.previous
        }
        println(manas)
    }

    @Test
    internal fun solve1() {
        val boss = Day22.Character.boss(71, 10)
        val player = Day22.Character.player(50, 500)
        println(Day22.solve1(player, boss))
    }

    @Test
    internal fun solve2() {
        val boss = Day22.Character.boss(71, 10)
        val player = Day22.Character.player(50, 500)
        println(Day22.solve2(player, boss))
    }

    @Test
    internal fun testExample1() {
        val boss = Day22.Character.boss(13, 8)
        val player = Day22.Character.player(10, 250)
        println(Day22.solve1(player, boss))
    }

    @Test
    internal fun testExample2() {
        val boss = Day22.Character.boss(14, 8)
        val player = Day22.Character.player(10, 250)
        println(Day22.solve1(player, boss))
    }
}