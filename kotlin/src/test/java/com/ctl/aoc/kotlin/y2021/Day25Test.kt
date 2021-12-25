package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day25Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2021, 25)

    val example = """v...>>.vv>
.vv>>.vv..
>>.>v>...v
>>v>>.>.v.
v>v.vv.v..
>.>>..v...
.vv..>.>v.
v.v..>>v.v
....v..v.>""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day25.solve1(example))
        println(Day25.solve1(puzzleInput))
    }

}