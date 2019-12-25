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


    @Test
    internal fun score() {
        assertThat(Day24.State.parse(scoreExample).rating())
                .isEqualTo(2129920L)
    }

    @Test
    fun solve1() {
        println(Day24.solve1(example1))
        println(Day24.solve1(puzzleInput))
    }
}