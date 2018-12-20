package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test
import com.ctl.aoc.kotlin.utils.Position
import org.junit.jupiter.api.Assertions.assertEquals

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
//        println(Day20.computePaths(input, Position(0, 0)))
    }

    @Test
    fun solve1() {
//        assertEquals(3, Day20.solve1(example0))
//        assertEquals(10, Day20.solve1(example1))
//        assertEquals(18, Day20.solve1(example2))
//        assertEquals(23, Day20.solve1(example3))
//        assertEquals(31, Day20.solve1(example4))

        println(Day20.solve1(input))
    }
}