package com.ctl.aoc.kotlin.y2019

object Day16 {

    val pattern = sequenceOf(0, 1, 0, -1)

    fun solve1(signal: String): String {
        return applyFFT(100, signal).take(8)
    }

    private val patternCache = mutableMapOf<Int, IntArray>()

    private fun patterForIndex(idx: Int): Sequence<Int> {
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

    private fun cachedPatternForIndex(size: Int, idx: Int): IntArray {
        return patternCache.computeIfAbsent(idx) { arrayPattern(size, it) }
    }

    fun arrayPattern(size: Int, idx: Int): IntArray {
        val pattern = patterForIndex(idx)
        val iterator = pattern.iterator()
        return IntArray(size) {
            iterator.next()
        }
    }

    private fun processOneDigit(idx: Int, signal: IntArray): Int {
        return signal.zip(cachedPatternForIndex(size = signal.size, idx = idx)) { x, y ->
            x * y
        }.sum().toString().last().toInt() - '0'.toInt()
    }

    private fun applyFFTOnce(signal: IntArray): IntArray {
        signal.indices.forEach { idx ->
            signal[idx] = processOneDigit(idx, signal)
        }
        return signal
    }

    private tailrec fun applyFFT(n: Int, signal: IntArray): IntArray {
        return if (n == 0) {
            signal
        } else {
            applyFFT(n - 1, applyFFTOnce(signal))
        }
    }

    private fun parseSignal(s: String): IntArray = s.map { it.toInt() - '0'.toInt() }.toIntArray()

    fun applyFFT(n: Int, signal: String): String {
        return applyFFT(n, parseSignal(signal)).joinToString("")
    }
}