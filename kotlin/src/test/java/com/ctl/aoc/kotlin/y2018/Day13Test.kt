package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day13Test{

    val input = InputUtils.getLines("2018", "day13.txt").toList()
    val example = InputUtils.getLines("2018", "day13Example.txt").toList()
    val jaques = InputUtils.getLines("2018", "day_13_Jaques.txt").toList()
    val example2 = InputUtils.getLines("2018", "day13Example2.txt").toList()

    val crash = InputUtils.getLines("2018", "Day13_crash.txt").toList()

    @Test
    internal fun solve1() {
        var s = Day13.solve1(example)
        println()
//        assertEquals("", Day13.solve1(sample))
    }

    @Test
    internal fun solve2() {
//        assertEquals(Day13.Position(6, 4),Day13.solve2(example2))
//        Day13.debug(crash, 20, true)
        println(Day13.solve2(input)) //not 128,20, not 64,76, not 111/139, not 62,60, not 41,55
//        println(Day13.solve2(jaques)) //not 128,20
    }
}