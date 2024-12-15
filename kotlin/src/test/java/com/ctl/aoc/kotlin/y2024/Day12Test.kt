package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day12Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2024, 12)

    val example1 = """AAAA
BBCD
BBCC
EEEC""".lineSequence()

    val example2= """OOOOO
OXOXO
OOOOO
OXOXO
OOOOO""".lineSequence()

    val example3 = """EEEEE
EXXXX
EEEEE
EXXXX
EEEEE""".lineSequence()

    val example4 = """RRRRIICCFF
RRRRIICCCF
VVRRRCCFFF
VVRCCCJFFF
VVVVCJJCFE
VVIVCCJJEE
VVIIICJJEE
MIIIIIJJEE
MIIISIJEEE
MMMISSJEEE""".lineSequence()

    @Test
    fun solve1() {
        println(Day12.solve1(example1))
        println(Day12.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day12.solve2(example1))
        println(Day12.solve2(example2))
        println(Day12.solve2(puzzleInput))
    }
}
