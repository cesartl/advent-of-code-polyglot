package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day16Test {

    val input = InputUtils.getLines(2015, 16)

    val scanResult = """children: 3
cats: 7
samoyeds: 2
pomeranians: 3
akitas: 0
vizslas: 0
goldfish: 5
trees: 3
cars: 2
perfumes: 1""".split("\n").map { Day16.Trait.parse(it) }.let { Day16.ScanResult(it.map { it.name to it.n }.toMap()) }

    @Test
    fun solve1() {
        println(Day16.solve1(scanResult, input))
    }

    @Test
    fun solve2() {
        println(Day16.solve2(scanResult, input))
    }
}