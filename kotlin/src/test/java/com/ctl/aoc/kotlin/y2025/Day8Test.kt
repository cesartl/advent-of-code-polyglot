package com.ctl.aoc.kotlin.y2025

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day8Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2025, 8)

    val example = """162,817,812
57,618,57
906,360,560
592,479,940
352,342,300
466,668,158
542,29,236
431,825,988
739,650,466
52,470,668
216,146,977
819,987,18
117,168,530
805,96,715
346,949,466
970,615,88
941,993,340
862,61,35
984,92,344
425,690,689""".lineSequence()

    @Test
    fun solve1() {
        println(Day8.solve1(example))
        println(Day8.solve1(puzzleInput, 1000))
    }

    @Test
    fun solve2() {
        println(Day8.solve2(example))
        println(Day8.solve2(puzzleInput))
    }
}
