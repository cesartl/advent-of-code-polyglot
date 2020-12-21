package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day21Test {

    val puzzleInput = InputUtils.getLines(2020, 21)

    val example = """mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
trh fvjkl sbzzf mxmxvkd (contains dairy)
sqjhc fvjkl (contains soy)
sqjhc mxmxvkd sbzzf (contains fish)""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day21.solve1(example))
        println(Day21.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        Day21.solve2(puzzleInput)
    }
}