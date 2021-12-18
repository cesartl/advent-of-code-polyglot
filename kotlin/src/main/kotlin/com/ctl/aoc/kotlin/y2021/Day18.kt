package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.y2021.Day18.SnailNumber.PairNumber
import com.ctl.aoc.kotlin.y2021.Day18.SnailNumber.RegularNumber

object Day18 {

    sealed class SnailNumber {
        data class RegularNumber(val n: Long) : SnailNumber() {
            override fun toString(): String = this.n.toString()
        }

        data class PairNumber(val left: SnailNumber, val right: SnailNumber) : SnailNumber() {
            override fun toString(): String = "[$left,$right]"
        }
    }

    private fun SnailNumber.applyDiffs(indexOffset: Int, vararg diffs: Pair<Int, Long>): Pair<SnailNumber, Int> {
        return when (this) {
            is RegularNumber -> {
                return (diffs.find { (index, _) -> index == indexOffset }?.let { (_, diff) ->
                    RegularNumber(n = this.n + diff)
                } ?: this) to (indexOffset + 1)
            }
            is PairNumber -> {
                val (left, right) = this
                val (newLeft, leftOffset) = left.applyDiffs(indexOffset, *diffs)
                val (newRight, rightOffset) = right.applyDiffs(leftOffset, *diffs)
                PairNumber(newLeft, newRight) to rightOffset
            }
        }
    }

    data class ExplodeMatch(val index: Int, val left: Long, val right: Long)

    data class ExplodeResult(val node: SnailNumber, val indexOffset: Int, val match: ExplodeMatch? = null)

    private fun SnailNumber.explodeSearch(depth: Int = 0, indexOffset: Int): ExplodeResult {
        return if (depth == 4) {
            when (this) {
                is PairNumber -> {
                    val leftN = (this.left as RegularNumber).n
                    val rightN = (this.right as RegularNumber).n
                    ExplodeResult(RegularNumber(0), indexOffset, ExplodeMatch(indexOffset, leftN, rightN))
                }
                is RegularNumber -> ExplodeResult(this, indexOffset + 1)
            }
        } else {
            when (this) {
                is RegularNumber -> ExplodeResult(this, indexOffset + 1)
                is PairNumber -> {
                    val leftSearch = left.explodeSearch(depth + 1, indexOffset)
                    if (leftSearch.match != null) {
                        return leftSearch.copy(node = this.copy(left = leftSearch.node))
                    }
                    val rightSearch = right.explodeSearch(depth + 1, leftSearch.indexOffset)
                    return rightSearch.copy(node = this.copy(right = rightSearch.node))
                }
            }
        }
    }

    fun SnailNumber.explode(): Pair<SnailNumber, Boolean> {
        val result = this.explodeSearch(0, 0)
        return result.match?.let { (index, leftN, rightN) ->
            result.node.applyDiffs(0, index - 1 to leftN, index + 1 to rightN).first to true
        } ?: (this to false)
    }

    fun Long.split(): Pair<Long, Long> {
        val d = this / 2
        val r = this % 2
        return d to d + r
    }

    fun SnailNumber.split(): Pair<SnailNumber, Boolean> {
        return when (this) {
            is RegularNumber -> {
                if (this.n >= 10) {
                    val (l, r) = this.n.split()
                    PairNumber(RegularNumber(l), RegularNumber(r)) to true
                } else {
                    this to false
                }
            }
            is PairNumber -> {
                val (newLeft, leftSplit) = left.split()
                if (leftSplit) {
                    return this.copy(left = newLeft) to true
                }
                val (newRight, rightSplit) = right.split()
                return this.copy(right = newRight) to rightSplit
            }
        }
    }

    private tailrec fun SnailNumber.reduce(): SnailNumber {
        val (afterExplode, isExplode) = this.explode()
        return if (isExplode) {
            afterExplode.reduce()
        } else {
            val (afterSplit, isSplit) = this.split()
            if (isSplit) {
                afterSplit.reduce()
            } else {
                this
            }
        }
    }

    operator fun SnailNumber.plus(other: SnailNumber): SnailNumber {
        return PairNumber(this, other).reduce()
    }


    data class SnailParser(val source: String) {
        var start = 0
        var current = 0

        fun parse(): SnailNumber {
            start = current
            val c = advance()
            return when {
                c == '[' -> {
                    val left = parse()
                    assert(advance() == ',')
                    val right = parse()
                    val pair = PairNumber(left, right)
                    assert(advance() == ']')
                    pair
                }
                c.isDigit() -> {
                    while (peek().isDigit()) {
                        advance()
                    }
                    RegularNumber(source.substring(start, current).toLong())
                }
                else -> {
                    error("Unknown car $c")
                }
            }
        }

        private fun advance(): Char = source[current++]
        private fun peek(): Char = source[current]
    }

    fun String.toSnailNumber() = SnailParser(this).parse()

    private fun SnailNumber.magnitude(): Long = when (this) {
        is RegularNumber -> this.n
        is PairNumber -> 3 * this.left.magnitude() + 2 * this.right.magnitude()
    }

    fun solve1(input: Sequence<String>): Long {
        val numbers = input.map { it.toSnailNumber() }
        val first = numbers.first()
        val result = numbers.drop(1).fold(first) { acc, next -> acc + next }
        return result.magnitude()
    }

    fun solve2(input: Sequence<String>): Long {
        val numbers = input.map { it.toSnailNumber() }
        return sequence {
            numbers.forEach { n1 ->
                numbers.forEach { n2 ->
                    if (n1 != n2) {
                        yield(n1 + n2)
                    }
                }
            }
        }.maxOf { it.magnitude() }
    }
}