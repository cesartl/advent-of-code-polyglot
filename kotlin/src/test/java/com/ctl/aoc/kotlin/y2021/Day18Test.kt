package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import com.ctl.aoc.kotlin.y2021.Day18.explode
import com.ctl.aoc.kotlin.y2021.Day18.plus
import com.ctl.aoc.kotlin.y2021.Day18.split
import com.ctl.aoc.kotlin.y2021.Day18.toSnailNumber
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class Day18Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2021, 18)

    val example = """[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
[[[5,[2,8]],4],[5,[[9,9],0]]]
[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
[[[[5,4],[7,7]],8],[[8,3],8]]
[[9,3],[[9,9],[6,[4,9]]]]
[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]""".splitToSequence("\n")

    @Test
    internal fun snailNumber() {
        checkParse("[[1,9],[8,5]]")
        checkParse("[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]")
    }

    @Test
    internal fun testAdd() {
        assertEquals("[[1,2],[[3,4],5]]", ("[1,2]".toSnailNumber() + "[[3,4],5]".toSnailNumber()).toString())
    }

    @Test
    internal fun explode() {
        testExplode("[[[[0,9],2],3],4]", "[[[[[9,8],1],2],3],4]")
        testExplode("[7,[6,[5,[7,0]]]]", "[7,[6,[5,[4,[3,2]]]]]")
        testExplode("[[6,[5,[7,0]]],3]", "[[6,[5,[4,[3,2]]]],1]")
        testExplode("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]", "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]")
        testExplode("[[3,[2,[8,0]]],[9,[5,[7,0]]]]", "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")
    }

    @Test
    internal fun testSplit() {
        testSplit("[[[[0,7],4],[[7,8],[0,13]]],[1,1]]","[[[[0,7],4],[15,[0,13]]],[1,1]]")
    }

    @Test
    internal fun testReduce() {
        val s1 = "[[[[4,3],4],4],[7,[[8,4],9]]]".toSnailNumber()
        val s2 = "[1,1]".toSnailNumber()
        assertEquals("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", (s1 + s2).toString())
    }

    @Test
    internal fun splitLong() {
        assertEquals((5L to 5L), (10L).split())
        assertEquals((5L to 6L), (11L).split())
    }

    private fun testExplode(expected: String, source: String) {
        val (exploded, found) = source.toSnailNumber().explode()
        assertTrue(found)
        assertEquals(expected, exploded.toString())
    }

    private fun testSplit(expected: String, source: String) {
        val (result, isSplit) = source.toSnailNumber().split()
        assertTrue(isSplit)
        assertEquals(expected, result.toString())
    }

    fun checkParse(s: String) {
        assertEquals(s, s.toSnailNumber().toString())
    }

    @Test
    fun solve1() {
        println(Day18.solve1(example))
        println(Day18.solve1(puzzleInput))
    }

    @Test
    fun solve2() {
        println(Day18.solve2(example))
        println(Day18.solve2(puzzleInput))
    }
}