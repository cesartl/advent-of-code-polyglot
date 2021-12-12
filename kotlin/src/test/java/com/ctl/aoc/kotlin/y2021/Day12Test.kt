package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import com.ctl.aoc.kotlin.utils.timedMs
import org.junit.jupiter.api.Test

internal class Day12Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2021, 12)

    val example = """start-A
start-b
A-c
A-b
b-d
A-end
b-end""".splitToSequence("\n")

    val example2 ="""dc-end
HN-start
start-kj
dc-start
dc-HN
LN-dc
HN-end
kj-sa
kj-HN
kj-dc""".splitToSequence("\n")


    val example3 ="""fs-end
he-DX
fs-he
start-DX
pj-DX
end-zg
zg-sl
zg-pj
pj-he
RW-he
fs-DX
pj-RW
zg-RW
start-pj
he-WI
zg-he
pj-fs
start-RW""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day12.solve1(example))
        println(Day12.solve1(example2))
        println(Day12.solve1(example3))
        println(Day12.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day12.solve2(example))
        println(Day12.solve2(example2))
        println(Day12.solve2(example3))
        println(Day12.solve2(puzzleInput))
    }

    @Test
    internal fun timing() {
        timedMs {
            println(Day12.solve1(puzzleInput))
            println(Day12.solve2(puzzleInput))
        }.let { println(it.first) }
    }
}