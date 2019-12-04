package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day4Test {

    val input = InputUtils.getLines(2019, 4)



    @Test
    fun solve1() {
        assertThat(Day4.isPasswordCorrect("111111")).isTrue()
        assertThat(Day4.isPasswordCorrect("223450")).isFalse()
        assertThat(Day4.isPasswordCorrect("123789")).isFalse()
        println(Day4.solve1(146810, 612564))
    }

    @Test
    internal fun solve2() {
        assertThat(Day4.isPasswordCorrect2("112233")).isTrue()
        assertThat(Day4.isPasswordCorrect2("123444")).isFalse()
        assertThat(Day4.isPasswordCorrect2("1234444")).isFalse()
        assertThat(Day4.isPasswordCorrect2("111122")).isTrue()
        println(Day4.solve2(146810, 612564))
    }
}