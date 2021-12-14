package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import com.ctl.aoc.kotlin.y2021.Day14.insertAt
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day14Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2021, 14)

    val example = """NNCB

CH -> B
HH -> N
CB -> H
NH -> C
HB -> C
HC -> B
HN -> C
NN -> C
BH -> H
NC -> B
NB -> B
BN -> B
BB -> N
BC -> B
CC -> N
CN -> C""".splitToSequence("\n")

    @Test
    internal fun testInsert() {
        assertEquals("ABC", "AC".insertAt(1, "B"))
    }

    @Test
    fun findALl() {
        val rule = Day14.Rule("BB", "C")
        assertEquals(listOf(5, 6, 12), rule.findAllMatches("NBCCNBBBCBHCBB").toList())
//        assertEquals(listOf(0), Day14.Rule("NN", "").findAllMatches("NNCB").toList())
    }

    @Test
    fun solve1() {
        println(Day14.solve1(example))
        println(Day14.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day14.solve2(puzzleInput))
    }
}