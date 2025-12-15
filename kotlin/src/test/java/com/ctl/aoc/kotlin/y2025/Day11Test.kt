package com.ctl.aoc.kotlin.y2025

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day11Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2025, 11)

    val example = """aaa: you hhh
you: bbb ccc
bbb: ddd eee
ccc: ddd eee fff
ddd: ggg
eee: out
fff: out
ggg: out
hhh: ccc fff iii
iii: out""".lineSequence()

    val example2 = """svr: aaa bbb
aaa: fft
fft: ccc
bbb: tty
tty: ccc
ccc: ddd eee
ddd: hub
hub: fff
eee: dac
dac: fff
fff: ggg hhh
ggg: out
hhh: out""".lineSequence()

    @Test
    fun solve1() {
        println(Day11.solve1(example))
        println(Day11.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day11.solve2(example2))
        println(Day11.solve2(puzzleInput))
    }
}
