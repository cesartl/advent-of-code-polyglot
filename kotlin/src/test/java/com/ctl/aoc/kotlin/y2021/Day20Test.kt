package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day20Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2021, 20)

    val example = """..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#
        |#..#.
        |#....
        |##..#
        |..#..
        |..###""".trimMargin().splitToSequence("\n")

    @Test
    fun solve1() {
//        println(Day20.solve1(example))
        println(Day20.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
//        println(Day20.solve2(example))
        println(Day20.solve2(puzzleInput))
    }
}