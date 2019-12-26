package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class Day24Test {

    val puzzleInput = InputUtils.getLines(2019, 24)

    val example1 = """....#
#..#.
#..##
..#..
#....""".splitToSequence('\n')

    val scoreExample = """.....
.....
.....
#....
.#...""".splitToSequence('\n')

    val example2 = """....#
#..#.
#..##
..#..
#....""".splitToSequence('\n')

    @Test
    internal fun score() {
        assertThat(Day24.State.parse(scoreExample).rating())
                .isEqualTo(2129920L)
    }

    @Test
    fun solve1() {
        println(Day24.solve2(example1))
        println(Day24.solve2(puzzleInput))
    }

    @Test
    internal fun solve2() {
//        println(Day24.solve2(example2, 10))
        assertThat(Day24.solve2(example2, 10)).isEqualTo(99)
        println(Day24.solve2(puzzleInput, 200))
    }
}