package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day13Test {

    val puzzleInput = InputUtils.downloadAndGetString(2024, 13)

    val example = """Button A: X+94, Y+34
Button B: X+22, Y+67
Prize: X=8400, Y=5400

Button A: X+26, Y+66
Button B: X+67, Y+21
Prize: X=12748, Y=12176

Button A: X+17, Y+86
Button B: X+84, Y+37
Prize: X=7870, Y=6450

Button A: X+69, Y+23
Button B: X+27, Y+71
Prize: X=18641, Y=10279"""

    @Test
    fun solve1() {
        println(Day13.solve1(example))
        //>22427
        println(Day13.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day13.solve2(example))
        //<98958951401149
        // 98080815200063
        println(Day13.solve2(puzzleInput))
    }
}
