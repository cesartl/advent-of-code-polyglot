package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day2Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2024, 2)

    val example = """7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day2.solve1(example))
        println(Day2.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day2.solve2(example))
        //>322
        //>339
        println(Day2.solve2(puzzleInput))
    }
}
