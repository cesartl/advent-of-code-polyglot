package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class Day18Test {

    val puzzleInput = InputUtils.getLines(2019, 18)
    val puzzleInput2 = InputUtils.getLines("2019", "day18Bis.txt")

    val example1 = """########################
#f.D.E.e.C.b.A.@.a.B.c.#
######################.#
#d.....................#
########################""".splitToSequence('\n')

    val example2 = """########################
#...............b.C.D.f#
#.######################
#.....@.a.B.c.d.A.e.F.g#
########################""".splitToSequence('\n')

    val example3 = """#################
#i.G..c...e..H.p#
########.########
#j.A..b...f..D.o#
########@########
#k.E..a...g..B.n#
########.########
#l.F..d...h..C.m#
#################""".splitToSequence('\n')

    val example4 = """########################
#@..............ac.GI.b#
###d#e#f################
###A#B#C################
###g#h#i################
########################""".splitToSequence('\n')

    @Test
    fun solve1() {
        println("example 1")
        assertThat(Day18.solve1(example1)).isEqualTo(86)
        println("example 2")
        assertThat(Day18.solve1(example2)).isEqualTo(132)
        println("example 3")
        assertThat(Day18.solve1(example3)).isEqualTo(136)
        println("example 4")
        assertThat(Day18.solve1(example4)).isEqualTo(81)
//        println(Day18.solve1(puzzleInput))
    }

    val example21 = """###############
#d.ABC.#.....a#
######@#@######
###############
######@#@######
#b.....#.....c#
###############""".splitToSequence('\n')

    @Test
    internal fun solve2() {
//        assertThat(Day18.solve2(example21)).isEqualTo(32)
        println(Day18.solve2(puzzleInput2))
    }
}