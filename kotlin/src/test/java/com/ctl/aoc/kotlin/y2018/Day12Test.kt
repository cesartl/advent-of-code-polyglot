package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day12Test {
    val initial = ".##.##...#.###..#.#..##..###..##...####.#...#.##....##.#.#...#...###.........##...###.....##.##.##"
    val input = InputUtils.getLines("2018", "day12.txt")

    val exampleStart = "#..#.#..##......###...###"
    val exampleRules = """...## => #
..#.. => #
.#... => #
.#.#. => #
.#.## => #
.##.. => #
.#### => #
#.#.# => #
#.### => #
##.#. => #
##.## => #
###.. => #
###.# => #
####. => #""".split("\n").asSequence()

    @Test
    internal fun testParseRule() {
        val rule = Day12.parseRule("...## => #")
        assertEquals(Day12.Rule(listOf(-2L to false, -1L to false, 0L to false, 1L to true, 2L to true), true), rule)
    }

    @Test
    internal fun nextState() {
        var state = Day12.parseState(exampleStart)
        val rules = exampleRules.map { Day12.parseRule(it) }
        assertEquals(exampleStart, state.print())
        assertEquals("#...#....#.....#..#..#..#", Day12.nextGeneration(state, rules).print())
    }

    @Test
    internal fun testPart1() {
        assertEquals(325, Day12.solve1(exampleStart, exampleRules, 20))
        println(Day12.solve1(initial, input, 20))
    }

    @Test
    internal fun testPart2() {
        println(Day12.solve1(initial, input, 50000000000))
    }
}