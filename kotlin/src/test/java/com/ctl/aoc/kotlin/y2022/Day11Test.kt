package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day11Test {

    val puzzleInput = InputUtils.downloadAndGetString(2022, 11)

    val exampleInput = """Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1
"""

    @Test
    fun solve1() {
        println(Day11.solve1(exampleInput))
        println(Day11.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day11.solve2(exampleInput))
        println(Day11.solve2(puzzleInput))
    }
}
