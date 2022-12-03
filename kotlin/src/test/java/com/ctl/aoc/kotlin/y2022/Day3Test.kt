package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day3Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2022, 3)

    val testInput = """vJrwpWtwJgWrhcsFMMfFFhFp
jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
PmmdzqPrVvPwwTWBwg
wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
ttgJtRGJQctTZtZT
CrZsJsPPZsGzwwsLwLmpwMDw""".splitToSequence("\n")


    @Test
    fun solve1() {
        println(Day3.solve1(testInput))
        println(Day3.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day3.solve2(testInput))
        println(Day3.solve2(puzzleInput))
    }
}
