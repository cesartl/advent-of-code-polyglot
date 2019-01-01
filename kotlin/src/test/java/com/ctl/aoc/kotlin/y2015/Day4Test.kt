package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.Strings
import org.junit.jupiter.api.Test

internal class Day4Test {

    val input = "iwrupvqb"

    @Test
    fun solve1() {
        println(Strings.md5("abcdef609043"))
//        assertEquals(609043, Day4.solve1("abcdef"))
//        assertEquals(1048970, Day4.solve1("pqrstuv"))
        println(Day4.solve1(input))
    }

    @Test
    internal fun solve2() {
        println(Day4.solve2(input))
    }
}