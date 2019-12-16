package com.ctl.aoc.kotlin.y2019

object Day16 {

    val pattern = sequenceOf(0, 1, 0, -1)

    fun solve1(numbers: Sequence<Int>): Int {

        TODO()
    }

    fun patterForIndex(idx: Int): Sequence<Int> {
        return sequence {
            while (true) {
                pattern.forEach { value ->
                    (0..idx).forEach { _ ->
                        yield(value)
                    }
                }
            }
        }.drop(1)
    }

    fun processOneDigit(idx: Int, signal: Sequence<Int>): Int {
        return signal.zip(patterForIndex(idx)) { x, y ->
            x * y
        }.sum().toString().last().toInt() - '0'.toInt()
    }

    fun applyFFTOnce(signal: Sequence<Int>): Sequence<Int> {
        return signal.mapIndexed { idx, _ ->
            processOneDigit(idx, signal)
        }
    }

    tailrec fun applyFFT(n: Int, signal: Sequence<Int>): Sequence<Int> {
        return if (n == 0) {
            signal
        } else {
            applyFFT(n - 1, applyFFTOnce(signal))
        }
    }

    fun parseSignal(s: String): Sequence<Int> = s.map { it.toInt() - '0'.toInt() }.asSequence()

    fun applyFFT(n: Int, signal: String): String {
        return applyFFT(n, parseSignal(signal)).joinToString("")
    }
}