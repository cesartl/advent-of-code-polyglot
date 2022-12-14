package com.ctl.aoc.kotlin.y2022

object Day13 {

    sealed class Signal {
        data class Single(val i: Int) : Signal()
        data class Multiple(val values: List<Signal>) : Signal()

        fun describe(): String = when (this) {
            is Multiple -> this.values.joinToString(",", prefix = "[", postfix = "]") { it.describe() }
            is Single -> this.i.toString()
        }
    }

    class SignalParser(val source: String) {
        var start = 0
        var current = 0

        fun parse(): Signal {
            start = current
            val c = advance()
            return when {
                c == '[' -> {
                    val values = mutableListOf<Signal>()
                    while (peek() != ']') {
                        values.add(parse())
                        if (peek() == ',') {
                            advance()
                        }
                    }
                    assert(advance() == ']')
                    Signal.Multiple(values)
                }

                c.isDigit() -> {
                    while (peek().isDigit()) {
                        advance()
                    }
                    Signal.Single(source.substring(start, current).toInt())
                }

                else -> error("Unknown char: '$c', source: $source, current: $current")
            }
        }

        private fun advance(): Char {
            if (current == source.length) {
                error("source: $source, current: $current")
            }
            return source[current++]
        }

        private fun peek(): Char = source[current]
    }

    fun String.parseSignal(): Signal {
        return SignalParser(this).parse()
    }

    private fun String.parsePairs(): Sequence<Pair<Signal, Signal>> {
        return this.splitToSequence("\n\n")
            .map { block ->
                val s = block.split("\n").map { it.parseSignal() }
                s[0] to s[1]
            }
    }

    fun Signal.correctOrder(right: Signal): Boolean? {
        return when (val left = this) {
            is Signal.Single -> when (right) {
                is Signal.Single ->
                    if (left.i < right.i) {
                        true
                    } else if (left.i > right.i) {
                        false
                    } else {
                        null
                    }

                is Signal.Multiple -> Signal.Multiple(listOf(left)).correctOrder(right)
            }

            is Signal.Multiple -> when (right) {
                is Signal.Single -> left.correctOrder(Signal.Multiple(listOf(right)))
                is Signal.Multiple -> {
                    var i = 0
                    var r: Boolean? = null
                    while (r == null) {
                        if (i == left.values.size && i == right.values.size) {
                            return null
                        }
                        if (i >= left.values.size) {
                            r = true
                            break
                        }
                        if (i >= right.values.size) {
                            r = false
                            break
                        }
                        r = left.values[i].correctOrder(right.values[i])
                        i++
                    }
                    r
                }
            }
        }
    }

    fun solve1(input: String): Int {
        return input.parsePairs()
            .withIndex()
            .filter {
                val (left, right) = it.value
                left.correctOrder(right) == true
            }
            .sumOf { it.index + 1 }
    }

    fun solve2(input: String): Int {
        val allSignals = mutableListOf<Signal>()
        input.parsePairs()
            .flatMap { sequenceOf(it.first, it.second) }
            .toList()
            .let { allSignals.addAll(it) }

        val k2 = "[[2]]".parseSignal()
        val k6 = "[[6]]".parseSignal()

        allSignals.add(k2)
        allSignals.add(k6)

        val sorted = allSignals.sortedWith { left, right ->
            if (left.correctOrder(right) == false) {
                1
            } else -1
        }
        val i1 = sorted.indexOf(k2)
        val i6 = sorted.indexOf(k6)
        return (i1 + 1) * (i6 + 1)
    }
}
