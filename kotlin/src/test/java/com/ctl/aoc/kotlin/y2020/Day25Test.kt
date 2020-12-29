package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.InputUtils
import com.ctl.aoc.kotlin.utils.timedMs
import org.junit.jupiter.api.Test

internal class Day25Test {

    val puzzleInput = InputUtils.getLines(2020, 25)

    val example = """5764801
17807724
    """.splitToSequence("\n")

    @Test
    fun solve1() {
        println(timedMs { Day25.solve1(example) })
        println(timedMs { Day25.solve1(puzzleInput, 10000000) })
    }

}