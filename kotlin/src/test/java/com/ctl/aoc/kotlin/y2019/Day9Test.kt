package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

internal class Day9Test {

    val puzzleInput = InputUtils.getString(2019, 9).split(",").map { it.toLong() }.toLongArray()

    val example1 = longArrayOf(109, 1, 204, -1, 1001, 100, 1, 100, 1008, 100, 16, 101, 1006, 101, 0, 99)
    val example2 = longArrayOf(1102, 34915192, 34915192, 7, 4, 7, 99, 0)
    val example3 = longArrayOf(104, 1125899906842624, 99)

    @Test
    internal fun examples() {
        val output = mutableListOf<Long>()
        Day9.run {
            val final = Day9.IntCodeState(intCode = example1.copyOf(999), output = { output.add(it) }).execute()
            assertThat(output).containsExactlyElementsOf(example1.toList())
        }

        output.clear()

        Day9.run {
            val final = Day9.IntCodeState(intCode = example2.copyOf(94915192), output = { output.add(it) }).execute()
            println(output)
        }

        output.clear()

        Day9.run {
            val final = Day9.IntCodeState(intCode = example3.copyOf(100), output = { output.add(it) }).execute()
            assertThat(output).containsExactlyElementsOf(listOf(1125899906842624L))
        }
    }

    @Test
    fun solve1() {
        val output = mutableListOf<Long>()
        val inputs = LinkedList(listOf(1L))
        Day9.run {
            val final = Day9.IntCodeState(intCode = puzzleInput.copyOf(2000), input = { inputs.remove() }, output = { output.add(it) }).execute()
            println(output)
        }
    }

    @Test
    fun testDebug() {
        val debug = longArrayOf(109, 19, 204, -34).copyOf(3000)
        debug[1985] = 1245
        Day9.run {
            val final = Day9.IntCodeState(intCode = debug, relativeBase = 2000).execute()
        }
    }

    @Test
    fun solve2() {
        val inputs = LinkedList(listOf(2L))
        val output = mutableListOf<Long>()
        Day9.run {
            val final = Day9.IntCodeState(intCode = puzzleInput.copyOf(2000), input = { inputs.remove() }, output = { output.add(it) }).execute()
            println(output)
        }
    }
}