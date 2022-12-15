package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day15Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2022, 15)

    val exampleInput = """Sensor at x=2, y=18: closest beacon is at x=-2, y=15
Sensor at x=9, y=16: closest beacon is at x=10, y=16
Sensor at x=13, y=2: closest beacon is at x=15, y=3
Sensor at x=12, y=14: closest beacon is at x=10, y=16
Sensor at x=10, y=20: closest beacon is at x=10, y=16
Sensor at x=14, y=17: closest beacon is at x=10, y=16
Sensor at x=8, y=7: closest beacon is at x=2, y=10
Sensor at x=2, y=0: closest beacon is at x=2, y=10
Sensor at x=0, y=11: closest beacon is at x=2, y=10
Sensor at x=20, y=14: closest beacon is at x=25, y=17
Sensor at x=17, y=20: closest beacon is at x=21, y=22
Sensor at x=16, y=7: closest beacon is at x=15, y=3
Sensor at x=14, y=3: closest beacon is at x=15, y=3
Sensor at x=20, y=1: closest beacon is at x=15, y=3""".splitToSequence("\n")

//    @Test
//    fun solve1() {
//        println(Day15.solve1(exampleInput))
//        println(Day15.solve1(puzzleInput, 2000000))
//    }

    @Test
    fun solve2() {
        println(Day15.solve2(exampleInput, 20))
        println(Day15.solve2(puzzleInput, 4000000))
    }

    @Test
    fun solve2Bis() {
//        println(Day15.solve2Bis(exampleInput, 20))
        println(Day15.solve2Bis(puzzleInput, 4000000))
    }
}
