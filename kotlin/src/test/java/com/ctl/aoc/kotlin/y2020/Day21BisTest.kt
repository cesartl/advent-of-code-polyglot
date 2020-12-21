package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day21BisTest {

    val puzzleInput = InputUtils.getLines(2020, 21)

    val example = """mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
trh fvjkl sbzzf mxmxvkd (contains dairy)
sqjhc fvjkl (contains soy)
sqjhc mxmxvkd sbzzf (contains fish)""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day21Bis.solve1(example))
        println(Day21Bis.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day21Bis.solve2(example))
        println(Day21Bis.solve2(puzzleInput))
    }
}