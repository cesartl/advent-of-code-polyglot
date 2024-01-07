package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day22Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2023, 22)

    val example = """1,0,1~1,2,1
0,0,2~2,0,2
0,2,3~2,2,3
0,0,4~0,2,4
2,0,5~2,2,5
0,1,6~2,1,6
1,1,8~1,1,9""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day22.solve1(example))
//        println(Day22.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day22.solve2(example))
        println(Day22.solve2(puzzleInput))
    }
}
