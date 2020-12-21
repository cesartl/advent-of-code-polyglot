package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day20Test {

    val puzzleInput = InputUtils.getString(2020, 20)
    val example = InputUtils.getString("2020", "day20-example.txt")

    val tileExample = """Tile 2311:
..##.#..#.
##..#.....
#...##..#.
####.#...#
##.##.###.
##...#.###
.#.#.#..##
..#....#..
###...#.#.
..###..###"""

    val tileExample2 = """Tile 999:
..#
..#
#.#"""

    @Test
    internal fun tile() {
        val tile0 = Day20.Tile.parse(tileExample)
        val tile1 = Day20.Tile.parse(tileExample2)
        tile1.allVariations().toList().forEach {
            it.print()
        }

        tile0.allBorders().toSet()
    }

    @Test
    fun solve1() {
        println(Day20.solve1(example))
        println(Day20.solve1(puzzleInput))
    }

    @Test
    internal fun name() {
        (0 until 3).forEach {
            println(it)
        }
    }

    @Test
    fun solve2() {
        println(Day20.solve2(example))
        println(Day20.solve2(puzzleInput))
    }
}