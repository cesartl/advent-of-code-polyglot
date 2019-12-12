package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day12Test {

    val input = InputUtils.getLines(2019, 12)

    val example = """<x=-1, y=0, z=2>
<x=2, y=-10, z=-7>
<x=4, y=-8, z=8>
<x=3, y=5, z=-1>""".split("\n").map { Day12.Vector.parse(it) }.toList()

    @Test
    internal fun testTick() {
        val system = Day12.JupiterSystem(example.map { Day12.Moon(it) })
        val ticked = Day12.doTicks(system, 10)
        assertThat(ticked.totalEnergy()).isEqualTo(179)
    }

    @Test
    fun solve1() {
        println(Day12.solve1(input))
    }

    @Test
    internal fun solve2() {
        val system = Day12.JupiterSystem(example.map { Day12.Moon(it) })
        assertThat(system.findCycle()).isEqualTo(2772)
        println(Day12.solve2(input))
    }
}