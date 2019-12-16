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
        assertThat(Day16.solve1(puzzleInput)).isEqualTo("88323090")
    }

    @Test
    internal fun solve2() {
        assertThat(Day16.solve2("03036732577212944063491565474664")).isEqualTo("84462026")
        assertThat(Day16.solve2("02935109699940807407585447034323")).isEqualTo("78725270")
        assertThat(Day16.solve2("03081770884921959731165446850517")).isEqualTo("53553731")
        println(Day16.solve2(puzzleInput))
    }

    @Test
    internal fun fft() {
        assertThat(Day16.applyFFT(1, "12345678").joinToString("")).isEqualTo("48226158")
        assertThat(Day16.applyFFT(2, "12345678").joinToString("")).isEqualTo("34040438")
        assertThat(Day16.applyFFT(3, "12345678").joinToString("")).isEqualTo("03415518")
        assertThat(Day16.applyFFT(4, "12345678").joinToString("")).isEqualTo("01029498")
    }
}