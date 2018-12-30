package com.ctl.aoc.kotlin.y2016

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day22Test {

    val input = InputUtils.getLines(2016, 22)

    val example = """
        Filesystem            Size  Used  Avail  Use%
/dev/grid/node-x0-y0   10T    8T     2T   80%
/dev/grid/node-x0-y1   11T    6T     5T   54%
/dev/grid/node-x0-y2   32T   28T     4T   87%
/dev/grid/node-x1-y0    9T    7T     2T   77%
/dev/grid/node-x1-y1    8T    0T     8T    0%
/dev/grid/node-x1-y2   11T    7T     4T   63%
/dev/grid/node-x2-y0   10T    6T     4T   60%
/dev/grid/node-x2-y1    9T    8T     1T   88%
/dev/grid/node-x2-y2    9T    6T     3T   66%""".split("\n").asSequence()

    @Test
    fun solve1() {
        println(Day22.solve1(input))
    }

    @Test
    internal fun solve2() {
        assertEquals(7, Day22.solve2(example))
        println(Day22.solve2(input)) // < 764
    }
}