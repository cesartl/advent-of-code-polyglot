package com.ctl.aoc.kotlin.y2017

import com.ctl.aoc.kotlin.utils.InputUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class Day1p1KtTest {

    @Test
    fun captcha() {
        assertThat(Day1p1.captcha("1122")).isEqualTo(3)
        println(Day1p1.captcha(InputUtils.getString("2017", "Day1.txt")))
    }
}