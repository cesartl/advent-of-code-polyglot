package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day10Test {

    val puzzleInput = InputUtils.getLines(2019, 10)

    val example0 = """.#..#
.....
#####
....#
...##""".split("\n").asSequence()

    val example1 = """......#.#.
#..#.#....
..#######.
.#.#.###..
.#..#.....
..#....#.#
#..#....#.
.##.#..###
##...#..#.
.#....####""".split("\n").asSequence()

    val example2 = """#.#...#.#.
.###....#.
.#....#...
##.#.#.#.#
....#.#.#.
.##..###.#
..#...##..
..##....##
......#...
.####.###.""".split("\n").asSequence()

    val example3 = """.#..#..###
####.###.#
....###.#.
..###.##.#
##.##.#.#.
....###..#
..#.#..#.#
#..#.#.###
.##...##.#
.....#.#..""".split("\n").asSequence()

    val example4 = """.#..##.###...#######
##.############..##.
.#.######.########.#
.###.#######.####.#.
#####.##.#.##.###.##
..#####..#.#########
####################
#.####....###.#.#.##
##.#################
#####.##.###..####..
..######..##.#######
####.##.####...##..#
.#####..#.######.###
##...#.##########...
#.##########.#######
.####.#.###.###.#.##
....##.##.###..#####
.#.#.###########.###
#.#.#.#####.####.###
###.##.####.##.#..##""".split("\n").asSequence()

    @Test
    internal fun testCoeff() {
        Day10.Coefficient(-3, 1).forward(Day10.Position(0, 0), Day10.Position(10, 10)).forEach { println(it) }
        Day10.Coefficient(-3, 1).backward(Day10.Position(0, 0), Day10.Position(10, 10)).forEach { println(it) }
    }

    @Test
    internal fun testCount() {
        assertThat(Day10.solve1(example0)).isEqualTo(8)
        assertThat(Day10.solve1(example1)).isEqualTo(33)
        assertThat(Day10.solve1(example2)).isEqualTo(35)
        assertThat(Day10.solve1(example3)).isEqualTo(41)
        assertThat(Day10.solve1(example4)).isEqualTo(210)
        println(Day10.solve1(puzzleInput))
    }

    @Test
    fun solve1() {
    }
}