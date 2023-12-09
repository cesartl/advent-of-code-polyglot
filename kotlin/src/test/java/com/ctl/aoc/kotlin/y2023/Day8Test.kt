package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.InputUtils
import com.ctl.aoc.kotlin.utils.lcm
import org.junit.jupiter.api.Test

internal class Day8Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2023, 8)

    val example = """RL

AAA = (BBB, CCC)
BBB = (DDD, EEE)
CCC = (ZZZ, GGG)
DDD = (DDD, DDD)
EEE = (EEE, EEE)
GGG = (GGG, GGG)
ZZZ = (ZZZ, ZZZ)""".splitToSequence("\n")

    val example2 = """LR

11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day8.solve1(example))
        println(Day8.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day8.solve2(example2))
        println(Day8.solve2(puzzleInput))
    }

    @Test
    fun testLcm() {
        val numbers = sequenceOf(
            19631,
            13771,
            21389,
            17287,
            23147,
            20803,
        ).map { it.toBigInteger() }
        println(lcm(numbers))
    }
}
