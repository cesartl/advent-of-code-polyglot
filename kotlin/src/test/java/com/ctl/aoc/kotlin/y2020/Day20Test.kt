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

    @Test
    internal fun tile() {
        val tile = Day20.Tile.parse(tileExample)
        val variations = tile.allVariations()
        println(variations.count())
        println(variations.toSet().size)
        variations.map { it.topBorder }.forEach {
            println(it)
        }
    }

    @Test
    fun solve1() {
        println(Day20.solve1(example))
        println(Day20.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day20.solve2(puzzleInput))
    }
}