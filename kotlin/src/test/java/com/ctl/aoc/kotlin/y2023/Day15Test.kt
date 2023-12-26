package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class Day15Test {

    val puzzleInput = InputUtils.downloadAndGetString(2023, 15)

    val example = """rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"""

    @Test
    fun solve1() {
        println(Day15.solve1(example))
        println(Day15.solve1(puzzleInput))
        //not 506311
    }

    @Test
    fun solve2() {
        println(Day15.solve2(example))
        println(Day15.solve2(puzzleInput))
    }

    @Test
    fun hashTest() {
        Assertions.assertEquals(52, "HASH".hash())
    }

    @Test
    fun linkedListTest() {
        val l: LinkedList<String> = Nil
        val x = l.append("a").append("b").append("c")
        println(x.toString())
        val y = l.add("a").add("b").add("c")
        println(y.toString())
    }
}
