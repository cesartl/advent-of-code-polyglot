package com.ctl.aoc.kotlin.y2019

import kotlin.math.abs

object Day16 {

    val pattern = sequenceOf(0, 1, 0, -1)

    fun solve1(signal: String): String {
        println("length ${signal.length}")
        return applyFFT(100, signal).joinToString("").take(8)
    }

    fun solve2(signal: String): String {
        val skip = signal.take(7).toInt()
        println("Offset: $skip")
        val bigSignal = multiplySignal(10000, signal)
        println("length after multiplication ${bigSignal.length}")
        val fft = applyFFTOptimised(100, parseSignal(bigSignal.drop(skip)))
        return buildString {
            (0 until 8).forEach { i ->
                append(fft[i])
            }
        }
    }

    private fun multiplySignal(n: Int, signal: String): String {
        return buildString {
            (0 until n).forEach { _ ->
                append(signal)
            }
        }
    }

    private val patternCache = mutableMapOf<String, LongArray>()

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

    private fun cachedPatternForIndex(size: Int, idx: Int, offset: Int): LongArray {
        return patternCache.computeIfAbsent("$size-$idx-$offset") { arrayPattern(size, idx, offset) }
    }

    fun arrayPattern(size: Int, idx: Int, offset: Int): LongArray {
        val pattern = patterForIndex(idx + offset).drop(offset)
        val iterator = pattern.iterator()
        return LongArray(size) {
            iterator.next().toLong()
        }
    }

    private fun processOneDigit(idx: Int, signal: LongArray, offset: Int): Long {
        val pattern = cachedPatternForIndex(size = signal.size, idx = idx, offset = offset)
        var count = 0L
        (signal.indices).forEach { i ->
            count += (signal[i] * pattern[i]) % 10
        }
        return abs(count) % 10L
    }

    private fun processOneDigitFast(idx: Int, signal: LongArray, total: Long): Long {
        TODO()
    }

    private fun applyFFTOnce(signal: LongArray, offset: Int): LongArray {
        (signal.indices).forEach { idx ->
            signal[idx] = processOneDigit(idx, signal, offset)
        }
        return signal
    }

    private fun applyFFTOptimisedOnce(signal: LongArray): LongArray {
        var sumSoFar = 0L
        (signal.indices).reversed().forEach { idx ->
            sumSoFar += signal[idx]
            signal[idx] = abs(sumSoFar) % 10L
        }
        return signal
    }

    private tailrec fun applyFFT(n: Int, signal: LongArray, offset: Int): LongArray {
        return if (n == 0) {
            signal
        } else {
            applyFFT(n - 1, applyFFTOnce(signal, offset), offset)
        }
    }

    private tailrec fun applyFFTOptimised(n: Int, signal: LongArray): LongArray {
        return if (n == 0) {
            signal
        } else {
            applyFFTOptimised(n - 1, applyFFTOptimisedOnce(signal))
        }
    }

    private fun parseSignal(s: String): LongArray = s.map { it.toLong() - '0'.toLong() }.toLongArray()

    fun applyFFT(n: Int, signal: String, offset: Int = 0): LongArray {
        println("Signal length ${signal.length}")
        return applyFFT(n, parseSignal(signal), offset)
    }
}