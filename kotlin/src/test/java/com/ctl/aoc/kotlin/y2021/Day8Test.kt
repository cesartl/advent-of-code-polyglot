package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.InputUtils
import com.ctl.aoc.kotlin.utils.timedMs
import org.junit.jupiter.api.Test

internal class Day8Test {

    val puzzleInput = InputUtils.downloadAndGetLines(2021, 8)

    val example = """be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce""".splitToSequence("\n")

    val example2 = """acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf"""

    @Test
    fun solve1() {
        println(Day8.solve1(example))
        println(Day8.solve1(puzzleInput))
    }

    @Test
    internal fun name() {
        val l = Day8.Line.parse(example2)
        val mapping = mapOf('d' to 'a', 'e' to 'b', 'a' to 'c', 'f' to 'd', 'g' to 'e', 'b' to 'f', 'c' to 'g')
        val findMapping = l.findMapping()
        println(findMapping)
        println(l.outputForMapping(mapping))
    }

    @Test
    fun solve2() {
        println(timedMs {Day8.solve2(example)})
        println(timedMs {Day8.solve2Bis(example)})
        println(timedMs {Day8.solve2(puzzleInput)})
        println(timedMs {Day8.solve2Bis(puzzleInput)})
    }
}