package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

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
        Day10.Vector(-3, 1).forward(Day10.Position(0, 0), Day10.Position(10, 10)).forEach { println(it) }
        Day10.Vector(-3, 1).backward(Day10.Position(0, 0), Day10.Position(10, 10)).forEach { println(it) }
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

    val part2Ex1 = """.#....#####...#..
##...##.#####..##
##...#...#.#####.
..#.....#...###..
..#.#.....#....##""".split("\n").asSequence()

    @Test
    fun solve2() {
        assertThat(Day10.solve2(example4)).isEqualTo(802)
        val t1= System.currentTimeMillis()
        assertThat(Day10.solve2(puzzleInput)).isEqualTo(616)
        val t2 = System.currentTimeMillis()
        println("${t2 - t1}ms")
    }
}