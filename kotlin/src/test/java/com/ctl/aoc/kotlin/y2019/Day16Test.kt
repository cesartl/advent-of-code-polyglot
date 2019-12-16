package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.InputUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class Day16Test {

    val puzzleInput = InputUtils.getString(2019, 16)

    @Test
    fun solve1() {
        println(Day16.applyFFT(2, puzzleInput))
    }

    @Test
    internal fun fft() {
        assertThat(Day16.applyFFTOnce(sequenceOf(1, 2, 3, 4, 5, 6, 7, 8)).toList())
                .isEqualTo(listOf(4, 8, 2, 2, 6, 1, 5, 8))

        assertThat(Day16.applyFFT(1, "12345678")).isEqualTo("48226158")
        assertThat(Day16.applyFFT(2, "12345678")).isEqualTo("34040438")
        assertThat(Day16.applyFFT(3, "12345678")).isEqualTo("03415518")
        assertThat(Day16.applyFFT(4, "12345678")).isEqualTo("01029498")
    }
}