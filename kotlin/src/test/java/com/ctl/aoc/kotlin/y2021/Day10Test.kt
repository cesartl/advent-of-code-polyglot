package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

internal class Day10Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2021, 10)

    val example = """[({(<(())[]>[[{[]{<()<>>
[(()[<>])]({[<{<<[]>>(
{([(<{}[<>[]}>{[]{[(<()>
(((({<>}<{<{<>}{[]{[]{}
[[<[([]))<([[{}[[()]]]
[{[{({}]{}}([{[{{{}}([]
{<[[]]>}<{[{[{[]{()[[[]
[<(<(<(<{}))><([]([]()
<{([([[(<>()){}]>(<<{{
<{([{{}}[<[[[<>{}]]]>[]]""".splitToSequence("\n")

    @Test
    internal fun testCorrupt() {
        assertNull(Day10.Line("([])").corruptedChar())
        assertNull(Day10.Line("{()()()}").corruptedChar())
        assertNull(Day10.Line("(((((((((())))))))))").corruptedChar())
        assertEquals(']', Day10.Line("(]").corruptedChar())
        assertEquals('>', Day10.Line("{()()()>").corruptedChar())
        assertEquals('}', Day10.Line("(((()))}").corruptedChar())
        assertEquals('}', Day10.Line("{([(<{}[<>[]}>{[]{[(<()>").corruptedChar())
    }

    @Test
    internal fun testComplete() {
        assertEquals("}}]])})]", Day10.Line("[({(<(())[]>[[{[]{<()<>>").complete().joinToString(separator = ""))
    }

    @Test
    fun solve1() {
        println(Day10.solve1(example))
        println(Day10.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day10.solve2(example))
        println(Day10.solve2(puzzleInput))
    }
}