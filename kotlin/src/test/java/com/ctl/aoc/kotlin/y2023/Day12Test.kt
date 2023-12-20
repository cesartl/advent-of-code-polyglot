package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Test

internal class Day12Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2023, 12)

    val example = """???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1""".splitToSequence("\n")

    @Test
    fun solve1() {
        println(Day12.solve1(example))
        println(Day12.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day12.solve2(puzzleInput))
    }
}
