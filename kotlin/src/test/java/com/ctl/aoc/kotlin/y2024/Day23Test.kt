package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day23Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2024, 23)

    val example = """kh-tc
qp-kh
de-cg
ka-co
yn-aq
qp-ub
cg-tb
vc-aq
tb-ka
wh-tc
yn-cg
kh-ub
ta-co
de-co
tc-td
tb-wq
wh-td
ta-ka
td-qp
aq-cg
wq-ub
ub-vc
de-ta
wq-aq
wq-vc
wh-yn
ka-de
kh-ta
co-tc
wh-qp
tb-vc
td-yn""".lineSequence()

    @Test
    fun solve1() {
        println(Day23.solve1(example))
        println(Day23.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day23.solve2(example))
        println(Day23.solve2(puzzleInput))
    }
}
