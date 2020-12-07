package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day7Test {

    val puzzleInput = InputUtils.getLines(2020, 7)

    val example = """light red bags contain 1 bright white bag, 2 muted yellow bags.
dark orange bags contain 3 bright white bags, 4 muted yellow bags.
bright white bags contain 1 shiny gold bag.
muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
dark olive bags contain 3 faded blue bags, 4 dotted black bags.
vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
faded blue bags contain no other bags.
dotted black bags contain no other bags.""".splitToSequence("\n")

    @Test
    fun solve1() {
//        println(Day7.solve1(example))
        println(Day7.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day7.solve2(example))
        println(Day7.solve2(puzzleInput))
    }
}