package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.InputUtils
import com.ctl.aoc.kotlin.utils.timedMs
import org.junit.jupiter.api.Test

internal class Day18Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2024, 18)

    val example = """5,4
4,2
4,5
3,0
2,1
6,3
2,4
1,5
0,6
3,3
2,6
5,1
1,2
5,5
2,5
6,5
1,4
0,4
6,4
1,1
6,1
1,0
0,5
1,6
2,0""".lineSequence()

    @Test
    fun solve1() {
        println(Day18.solve1(example, 6, 12))
        println(Day18.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(timedMs { Day18.solve2BinarySearch(puzzleInput) })
//        println(Day18.solve2(example, 6, 12))
        println(timedMs { Day18.solve2(puzzleInput) })
    }
}
