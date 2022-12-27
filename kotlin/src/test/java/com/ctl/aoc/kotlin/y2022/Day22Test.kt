package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day22Test {

    val puzzleInput = InputUtils.downloadAndGetString(2022, 22)

    val example = """        ...#
        .#..
        #...
        ....
...#.......#
........#...
..#....#....
..........#.
        ...#....
        .....#..
        .#......
        ......#.

10R5L5R10L4R5L5"""

    @Test
    fun solve1() {
        println(Day22.solve1(example))
        println(Day22.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        assertEquals(5031, Day22.solve2(example, 4))
        println(Day22.solve2(puzzleInput, 50))
    }
}
