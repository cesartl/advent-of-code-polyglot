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
        tile0.normalise().print()
        val tile = Day20.Tile.parse(tileExample2)
        val variations = tile.allVariations()
        tile.normalise().print()
        tile.flipV().normalise().print()
        tile.flipH().normalise().print()
//        tile.removeBorders().print()
//        tile.rotate90().normalise().print()
//        tile.rotate180().normalise().print()
//        tile.rotate270().normalise().print()
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