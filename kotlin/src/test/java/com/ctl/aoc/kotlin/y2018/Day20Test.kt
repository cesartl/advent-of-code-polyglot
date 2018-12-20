package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test
import com.ctl.aoc.kotlin.utils.Position

internal class Day20Test {

    val example0 = "^WNE\$"
    val example1 = "^ENWWW(NEEE|SSE(EE|N))\$"
    val example2 = "^ENNWSWW(NEWS|)SSSEEN(WNSE|)EE(SWEN|)NNN\$"
    val example3 = "^ESSWWN(E|NNENN(EESS(WNSE|)SSS|WWWSSSSE(SW|NNNE)))\$"
    val example4 = "^WSSEESWWWNW(S|NENNEEEENN(ESSSSW(NWSW|SSEN)|WSWWN(E|WWS(E|SS))))\$"
    val input = InputUtils.getString("2018", "day20.txt")

    @Test
    fun getPaths() {
//        println(Day20.getPaths(example1.drop(1), "").toList())
        println(Day20.getPaths(example1.drop(1), "").toList())
        println(Day20.getPaths(example2.drop(1), "").toList())
        println(Day20.getPaths(example3.drop(1), "").toList())
        println(Day20.getPaths(example4.drop(1), "").toList())
    }

    @Test
    internal fun testComputePaths() {
//        println(Day20.computePaths(example0, Position(0, 0)))
//        println(Day20.computePaths(example1, Position(0, 0)))
        println(Day20.computePaths(input, Position(0, 0)))
    }

    @Test
    fun solve1() {
        println(Day20.getPaths(input.drop(1), "").take(10).toList())
    }
}