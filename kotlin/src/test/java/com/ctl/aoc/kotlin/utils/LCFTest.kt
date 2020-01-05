package com.ctl.aoc.kotlin.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class LCFTest {

    @Test
    fun applyK() {
        val m = 10.toBigInteger()
        val lcf1 = LCF(1, -6, m)
        val lcf2 = LCF(7, 0, m)
        val lcf3 = LCF(-1, -1, m)
        val lcf = lcf1 andThen lcf2 andThen lcf3
        assertThat(lcf.applyK(1.toBigInteger())).isEqualTo(lcf)
        assertThat(lcf.applyK(2.toBigInteger())).isEqualTo(lcf andThen lcf)
        assertThat(lcf.applyK(3.toBigInteger())).isEqualTo(lcf andThen lcf andThen lcf)
        assertThat(lcf.applyK(4.toBigInteger())).isEqualTo(lcf andThen lcf andThen lcf andThen lcf)
    }
}