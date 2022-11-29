package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

internal class Day7Test {

    val puzzleInput = InputUtils.getString(2019, 7).split(",").map { it.toInt() }.toIntArray()
    val puzzleInputLong = InputUtils.getString(2019, 7).split(",").map { it.toLong() }.toLongArray()

    @Test
    fun solve1() {
        println(Day7.solve1(puzzleInput))
    }

    @Test
    internal fun runWithPhases() {
        assertThat(
            Day7.runWithPhases(
                intArrayOf(3, 15, 3, 16, 1002, 16, 10, 16, 1, 16, 15, 15, 4, 15, 99, 0, 0),
                listOf(4, 3, 2, 1, 0)
            )
        )
            .isEqualTo(43210)

        assertThat(
            Day7.runWithPhases(
                intArrayOf(
                    3, 23, 3, 24, 1002, 24, 10, 24, 1002, 23, -1, 23,
                    101, 5, 23, 23, 1, 24, 23, 23, 4, 23, 99, 0, 0
                ), listOf(0, 1, 2, 3, 4)
            )
        )
            .isEqualTo(54321)

        assertThat(
            Day7.runWithPhases(
                intArrayOf(
                    3, 31, 3, 32, 1002, 32, 10, 32, 1001, 31, -2, 31, 1007, 31, 0, 33,
                    1002, 33, 7, 33, 1, 33, 31, 31, 1, 32, 31, 31, 4, 31, 99, 0, 0, 0
                ), listOf(1, 0, 4, 3, 2)
            )
        )
            .isEqualTo(65210)
    }

    @Test
    internal fun testFeedbackLoop() {
        val ex1 = intArrayOf(
            3, 26, 1001, 26, -4, 26, 3, 27, 1002, 27, 2, 27, 1, 27, 26,
            27, 4, 27, 1001, 28, -1, 28, 1005, 28, 6, 99, 0, 0, 5
        )
        val ex2 = intArrayOf(
            3, 52, 1001, 52, -5, 52, 3, 53, 1, 52, 56, 54, 1007, 54, 5, 55, 1005, 55, 26, 1001, 54,
            -5, 54, 1105, 1, 12, 1, 53, 54, 53, 1008, 54, 0, 55, 1001, 55, 1, 55, 2, 53, 55, 53, 4,
            53, 1001, 56, -1, 56, 1005, 56, 6, 99, 0, 0, 0, 0, 10
        )

        assertThat(Day7.runWithFeedbackLoop(ex1, listOf(9, 8, 7, 6, 5))).isEqualTo(139629729)
        assertThat(Day7.runWithFeedbackLoop(ex2, listOf(9, 7, 8, 5, 6))).isEqualTo(18216)
    }

    @Test
    internal fun testFeedbackLoopChannel() {
        val ex1 = longArrayOf(
            3, 26, 1001, 26, -4, 26, 3, 27, 1002, 27, 2, 27, 1, 27, 26,
            27, 4, 27, 1001, 28, -1, 28, 1005, 28, 6, 99, 0, 0, 5
        )
        val ex2 = longArrayOf(
            3, 52, 1001, 52, -5, 52, 3, 53, 1, 52, 56, 54, 1007, 54, 5, 55, 1005, 55, 26, 1001, 54,
            -5, 54, 1105, 1, 12, 1, 53, 54, 53, 1008, 54, 0, 55, 1001, 55, 1, 55, 2, 53, 55, 53, 4,
            53, 1001, 56, -1, 56, 1005, 56, 6, 99, 0, 0, 0, 0, 10
        )

        runBlocking {
            assertThat(Day7.runWithFeedbackLoopChannel(ex1, listOf(9, 8, 7, 6, 5))).isEqualTo(139629729)
            assertThat(Day7.runWithFeedbackLoopChannel(ex2, listOf(9, 7, 8, 5, 6))).isEqualTo(18216)
        }
    }

    @Test
    internal fun solve2() {
        println(measureTimeMillis {
            println(Day7.solve2(puzzleInput)) //not 206580
        })
        println(measureTimeMillis {
            runBlocking {
                println(Day7.solve2Channel(puzzleInputLong)) //not 206580
            }
        })
    }
}
