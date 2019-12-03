package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day3Test {

    val input = InputUtils.getLines(2019, 3)

    @Test
    fun solve1() {
        val test = sequenceOf("R8,U5,L5,D3", "U7,R6,D4,L4")
        println(Day3.solve1(test));
        val test2 = sequenceOf("R75,D30,R83,U83,L12,D49,R71,U7,L72", "U62,R66,U55,R34,D71,R55,D58,R83")
        println(Day3.solve1(test2));

        println(Day3.solve1(input))
    }

    @Test
    fun solve2() {
        val test = sequenceOf("R8,U5,L5,D3", "U7,R6,D4,L4")
        println(Day3.solve2(test));
        val test2 = sequenceOf("R75,D30,R83,U83,L12,D49,R71,U7,L72", "U62,R66,U55,R34,D71,R55,D58,R83")
        println(Day3.solve2(test2));
        val test3 = sequenceOf("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51", "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")
        println(Day3.solve2(test3));

        println(Day3.solve2(input)) // not 386
    }
}