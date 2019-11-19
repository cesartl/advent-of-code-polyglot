package com.ctl.aoc.kotlin.y2015

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class Day25Test {

    @Test
    fun getIndex() {
        print(Day25.getIndex(1, 1))
        print(" ")
        print(Day25.getIndex(1, 2))
        print(" ")
        print(Day25.getIndex(1, 3))
        print(" ")
        print(Day25.getIndex(1, 4))
        print(" ")
        print(Day25.getIndex(1, 5))

        println()

        print(Day25.getIndex(2, 1))
        print(" ")
        print(Day25.getIndex(2, 2))
        print(" ")
        print(Day25.getIndex(2, 3))
        print(" ")
        print(Day25.getIndex(2, 4))
        print(" ")
        print(Day25.getIndex(2, 5))

        println()

        print(Day25.getIndex(3, 1))
        print(" ")
        print(Day25.getIndex(3, 2))
        print(" ")
        print(Day25.getIndex(3, 3))
        print(" ")
        print(Day25.getIndex(3, 4))
        print(" ")
    }

    @Test
    internal fun solve1() {
        (1..6).forEach { row ->
            (1..6).forEach { col ->
                print(Day25.solve1(row, col))
                print("\t\t")
            }
            println(" ")
        }

        println(Day25.solve1(2981, 3075))
    }
}