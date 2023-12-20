package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day10Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2023, 10)

    val ex0 = """.....
.F-7.
.|.|.
.L-J.
.....""".splitToSequence("\n")

    val ex1 = """-L|F7
7S-7|
L|7||
-L-J|
L|-JF""".splitToSequence("\n")

    val ex2 = """...........
.S-------7.
.|F-----7|.
.||.....||.
.||.....||.
.|L-7.F-J|.
.|..|.|..|.
.L--J.L--J.
...........""".splitToSequence("\n")

    val ex3 = """FF7FSF7F7F7F7F7F---7
L|LJ||||||||||||F--J
FL-7LJLJ||||||LJL-77
F--JF--7||LJLJ7F7FJ-
L---JF-JLJ.||-FJLJJ7
|F|F-JF---7F7-L7L|7|
|FFJF7L7F-JF7|JL---7
7-L-JL7||F7|L7F-7F7|
L.L7LFJ|||||FJL7||LJ
L7JLJL-JLJLJL--JLJ.L""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day10.solve1(ex1))
//        println(Day10.solve1(ex2))
        println(Day10.solve1(puzzleInput))
        //> 308
    }

    @Test
    fun solve2() {
        println(Day10.solve2(puzzleInput))
    }
}
