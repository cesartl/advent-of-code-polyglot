package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day24Test {

    val puzzleInput = InputUtils.getLines(2020, 24)

    val example = """sesenwnenenewseeswwswswwnenewsewsw
neeenesenwnwwswnenewnwwsewnenwseswesw
seswneswswsenwwnwse
nwnwneseeswswnenewneswwnewseswneseene
swweswneswnenwsewnwneneseenw
eesenwseswswnenwswnwnwsewwnwsene
sewnenenenesenwsewnenwwwse
wenwwweseeeweswwwnwwe
wsweesenenewnwwnwsenewsenwwsesesenwne
neeswseenwwswnwswswnw
nenwswwsewswnenenewsenwsenwnesesenew
enewnwewneswsewnwswenweswnenwsenwsw
sweneswneswneneenwnewenewwneswswnese
swwesenesewenwneswnwwneseswwne
enesenwswwswneneswsenwnewswseenwsese
wnwnesenesenenwwnenwsewesewsesesew
nenewswnwewswnenesenwnesewesw
eneswnwswnwsenenwnwnwwseeswneewsenese
neswnwewnwnwseenwseesewsenwsweewe
wseweeenwnesenwwwswnew""".splitToSequence("\n")


    @Test
    internal fun parse() {
        val parsed = example.map { line -> Day24.Direction.parseDirections(line).joinToString("") { it.toString().toLowerCase() } }.toList()

        assertEquals(parsed, example.toList())
    }

    @Test
    fun solve1() {
        println(Day24.solve1(example))
        println(Day24.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day24.solve2(example))
        println(Day24.solve2(puzzleInput))
    }
}