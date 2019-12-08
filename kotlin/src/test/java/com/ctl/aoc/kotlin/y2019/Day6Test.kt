package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class Day6Test {

    val puzzleInput = InputUtils.getLines(2019, 6)

    val example1 = """COM)B
B)C
C)D
D)E
E)F
B)G
G)H
D)I
E)J
J)K
K)L""".split("\n").asSequence()

    val example2 = """COM)B
B)C
C)D
D)E
E)F
B)G
G)H
D)I
E)J
J)K
K)L
K)YOU
I)SAN""".split("\n").asSequence()

    @Test
    fun solve1() {
        assertThat(Day6.solve1(example1)).isEqualTo(42)
        assertThat(Day6.solve1Bis(example1)).isEqualTo(42)
        println(Day6.solve1(puzzleInput))
        assertThat(Day6.solve1Bis(puzzleInput)).isEqualTo(106065)
    }


    @Test
    internal fun solve2() {
        println(Day6.solve2(example2))
        assertThat(Day6.solve2Bis(example2)).isEqualTo(4)

        val t1 = System.currentTimeMillis()
        assertThat(Day6.solve2(puzzleInput)).isEqualTo(253)
        val t2 = System.currentTimeMillis()
        assertThat(Day6.solve2Bis(puzzleInput)).isEqualTo(253)
        val t3 = System.currentTimeMillis()

        println("Solution 1: ${t2 - t1}ms")
        println("Solution 1bis: ${t3 - t2}ms")
    }
}