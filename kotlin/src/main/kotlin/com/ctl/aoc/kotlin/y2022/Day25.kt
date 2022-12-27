package com.ctl.aoc.kotlin.y2022

fun String.toSnafu() = Day25.Snafu(this)

object Day25 {

    @JvmInline
    value class Snafu(val digits: String) {
        fun charFromEnd(i: Int): Char {
            if (i >= digits.length) {
                return '0'
            }
            return digits[digits.length - i - 1]
        }
    }

    private val charValue = mapOf(
        '=' to -2,
        '-' to -1,
        '0' to 0,
        '1' to 1,
        '2' to 2,
    )

    private val reverseCharValue = charValue.asSequence().associate { it.value to it.key }

    operator fun Snafu.plus(other: Snafu): Snafu {
        val max = this.digits.length.coerceAtLeast(other.digits.length) + 1
        val buffer = CharArray(max)
        var i = 0
        var carry = 0
        while (i < max) {
            val a = this.charFromEnd(i)
            val b = other.charFromEnd(i)
            val sum = charValue[a]!! + charValue[b]!! + carry
            if (sum in -2..2) {
                buffer[buffer.size - 1 - i] = reverseCharValue[sum]!!
                carry = 0
            } else {
                when (sum) {
                    5 -> {
                        buffer[buffer.size - 1 - i] = '0'
                        carry = 1
                    }

                    4 -> {
                        buffer[buffer.size - 1 - i] = '-'
                        carry = 1
                    }

                    3 -> {
                        buffer[buffer.size - 1 - i] = '='
                        carry = 1
                    }

                    -3 -> {
                        buffer[buffer.size - 1 - i] = '2'
                        carry = -1
                    }
                    -4 -> {
                        buffer[buffer.size - 1 - i] = '1'
                        carry = -1
                    }

                    -5 ->{
                        buffer[buffer.size - 1 - i] = '0'
                        carry = -1
                    }

                    else -> error("sum: $sum")
                }
            }
            i++
        }
        return Snafu(buffer.joinToString(separator = "").trim())
    }

    fun solve1(input: Sequence<String>): String {
        return input.map { it.toSnafu() }
            .fold("0".toSnafu()) { acc, snafu -> acc + snafu }
            .digits
    }

    fun solve2(input: Sequence<String>): Int {
        TODO()
    }
}
