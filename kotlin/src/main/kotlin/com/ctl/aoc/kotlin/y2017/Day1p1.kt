package com.ctl.aoc.kotlin.y2017

object Day1p1 {
    fun captcha(s: String): Int {
        var total = 0
        for (i in s.indices) {
            var current = s[i] - '0'
            var next = s[(i + 1) % s.length] - '0'
            if (current == next) {
                total += current
            }
        }
        return total
    }

}

