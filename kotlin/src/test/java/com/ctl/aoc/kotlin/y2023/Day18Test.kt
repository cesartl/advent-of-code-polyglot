package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day18Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2023, 18)

    val example = """R 6 (#70c710)
D 5 (#0dc571)
L 2 (#5713f0)
D 2 (#d2c081)
R 2 (#59c680)
D 2 (#411b91)
L 5 (#8ceee2)
U 2 (#caa173)
L 1 (#1b58a2)
U 2 (#caa171)
R 2 (#7807d2)
U 3 (#a77fa3)
L 2 (#015232)
U 2 (#7a21e3)""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day18.solve1(example))
        println(Day18.solve1(puzzleInput)) //>35030 >48278 <49549
    }

    @Test
    fun solve2() {
        println(Day18.solve2(puzzleInput))
    }
}
