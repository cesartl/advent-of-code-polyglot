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
        println(Day6.solve1(puzzleInput))
    }


    @Test
    internal fun solve2() {
        println(Day6.solve2(example2))
        println(Day6.solve2(puzzleInput))
    }
}