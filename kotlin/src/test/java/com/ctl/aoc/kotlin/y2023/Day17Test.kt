package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day17Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2023, 17)

    val example = """2413432311323
3215453535623
3255245654254
3446585845452
4546657867536
1438598798454
4457876987766
3637877979653
4654967986887
4564679986453
1224686865563
2546548887735
4322674655533""".splitToSequence("\n")

    val example2 = """111111111111
999999999991
999999999991
999999999991
999999999991""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day17.solve1(example))
        println(Day17.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
//        println(Day17.solve2(example))
//        println(Day17.solve2(example2))
        println(Day17.solve2(puzzleInput)) //!895
    }
}
