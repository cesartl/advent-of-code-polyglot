package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day24Test {

    val example = """Immune System:
17 units each with 5390 hit points (weak to radiation, bludgeoning) with an attack that does 4507 fire damage at initiative 2
989 units each with 1274 hit points (immune to fire; weak to bludgeoning, slashing) with an attack that does 25 slashing damage at initiative 3

Infection:
801 units each with 4706 hit points (weak to radiation) with an attack that does 116 bludgeoning damage at initiative 1
4485 units each with 2961 hit points (immune to radiation; weak to fire, cold) with an attack that does 12 slashing damage at initiative 4""".split("\n").asSequence()

    val input = InputUtils.getLines("2018", "day24.txt")

    @Test
    fun solve1() {

        assertEquals(5216, Day24.solve1(example))
        println(Day24.solve1(input)) //> 19380

    }

    @Test
    internal fun solve2() {
        println(Day24.solve3(input)) //< 6615

    }
}