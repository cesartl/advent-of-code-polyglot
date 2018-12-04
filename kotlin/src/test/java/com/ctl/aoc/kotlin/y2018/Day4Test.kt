package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.InputUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day4Test{


    val example  = """[1518-11-01 00:00] Guard #10 begins shift
[1518-11-01 00:05] falls asleep
[1518-11-01 00:25] wakes up
[1518-11-01 00:30] falls asleep
[1518-11-01 00:55] wakes up
[1518-11-01 23:58] Guard #99 begins shift
[1518-11-02 00:40] falls asleep
[1518-11-02 00:50] wakes up
[1518-11-03 00:05] Guard #10 begins shift
[1518-11-03 00:24] falls asleep
[1518-11-03 00:29] wakes up
[1518-11-04 00:02] Guard #99 begins shift
[1518-11-04 00:36] falls asleep
[1518-11-04 00:46] wakes up
[1518-11-05 00:03] Guard #99 begins shift
[1518-11-05 00:45] falls asleep
[1518-11-05 00:55] wakes up""".split("\n").asSequence()

    @Test
    internal fun testParse() {
        val logs =example.map { Day4.parseGuardLog(it) }
        println(logs.toList())
    }

    @Test
    internal fun solve1() {
        val lines =InputUtils.getLines("2018", "day4.txt")
        println(Day4.solve1(example))
        println(Day4.solve1(lines))
    }

    @Test
    internal fun solve2() {
        val lines =InputUtils.getLines("2018", "day4.txt")
        println(Day4.solve2(example))
        println(Day4.solve2(lines)) //17212
    }
}