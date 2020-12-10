package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day10Test {

    val puzzleInput = InputUtils.getLines(2020, 10)

    val example = """16
10
15
5
1
11
7
19
6
12
4""".splitToSequence("\n")

    val bigExample = """28
33
18
42
31
14
46
20
48
47
24
23
49
45
19
38
39
11
1
32
25
35
8
17
7
9
4
2
34
10
3""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day10.solve1(example))
        println(Day10.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day10.solve2(example))
//        println(Day10.solve2(bigExample))
//        println(Day10.solve2(puzzleInput))
    }
}