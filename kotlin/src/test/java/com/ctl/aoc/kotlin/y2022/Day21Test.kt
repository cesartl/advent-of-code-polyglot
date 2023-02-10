package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day21Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2022, 21)

    val exampleInput = """root: pppw + sjmn
dbpl: 5
cczh: sllz + lgvd
zczc: 2
ptdq: humn - dvpt
dvpt: 3
lfqf: 4
humn: 5
ljgn: 2
sjmn: drzm * dbpl
sllz: 4
pppw: cczh / lfqf
lgvd: ljgn * ptdq
drzm: hmdt - zczc
hmdt: 32""".splitToSequence("\n")

//    @Test
//    fun solve1() {
//        println(Day21.solve1(exampleInput))
//        println(Day21.solve1(puzzleInput))
//    }

    @Test
    fun solve2() {
        println(Day21.solve2(puzzleInput))
    }
}
