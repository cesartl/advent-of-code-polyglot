package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class Day16Test {

    val puzzleInput = InputUtils.getString(2019, 16)

    @Test
    fun solve1() {
        assertThat(Day16.solve1("80871224585914546619083218645595")).isEqualTo("24176176")
        assertThat(Day16.solve1("19617804207202209144916044189917")).isEqualTo("73745418")
        assertThat(Day16.solve1("69317163492948606335995924319873")).isEqualTo("52432133")
        println(Day16.solve1(puzzleInput)) //> 80424724
    }

    @Test
    internal fun array() {
        println(Day16.arrayPattern(20, 2).toList())
    }

    @Test
    internal fun fft() {
        assertThat(Day16.applyFFT(1, "12345678")).isEqualTo("48226158")
        assertThat(Day16.applyFFT(2, "12345678")).isEqualTo("34040438")
        assertThat(Day16.applyFFT(3, "12345678")).isEqualTo("03415518")
        assertThat(Day16.applyFFT(4, "12345678")).isEqualTo("01029498")
    }
}