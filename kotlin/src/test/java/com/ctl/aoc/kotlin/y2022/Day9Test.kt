package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day9Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2022, 9)

    val exampleInput = """R 4
U 4
L 3
D 1
R 4
D 1
L 5
R 2""".splitToSequence("\n")

    val example2Input = """R 5
U 8
L 8
D 3
R 17
D 10
L 25
U 20""".splitToSequence("\n")

//    @Test
//    fun solve1() {
//        println(Day9.solve1(exampleInput))
//        println(Day9.solve1(puzzleInput))
//    }

    @Test
    fun solve2() {
        println(Day9.solve2(exampleInput))
        println(Day9.solve2(example2Input))
        println(Day9.solve2(puzzleInput))
    }
}
