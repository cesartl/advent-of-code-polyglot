package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day25Test {

    val puzzleInput = InputUtils.downloadAndGetString(2024, 25)

    val example = """#####
.####
.####
.####
.#.#.
.#...
.....

#####
##.##
.#.##
...##
...#.
...#.
.....

.....
#....
#....
#...#
#.#.#
#.###
#####

.....
.....
#.#..
###..
###.#
###.#
#####

.....
.....
.....
#....
#.#..
#.#.#
#####"""

    @Test
    fun solve1() {
        println(Day25.solve1(example))
        println(Day25.solve1(puzzleInput))
    }

}
