package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.LCF
import com.ctl.aoc.kotlin.utils.ModInt
import java.math.BigInteger
import java.util.*

typealias Deck = Sequence<Int>

object Day22 {

    sealed class ShuffleTechnique {
        abstract fun shuffle(deck: Deck): Deck
        abstract fun toLCF(m: BigInteger): LCF

        object DealNew : ShuffleTechnique() {
            override fun shuffle(deck: Deck): Deck {
                return sequence {
                    val stack: Deque<Int> = ArrayDeque()
                    deck.forEach { stack.addFirst(it) }
                    while (stack.isNotEmpty()) {
                        yield(stack.removeFirst())
                    }
                }
            }

            override fun toLCF(m: BigInteger) = LCF(-1L, -1L, m)

        }

        data class Cut(val n: Int, val original: Int = n) : ShuffleTechnique() {
            override fun shuffle(deck: Deck): Deck {
                val cut = deck.take(n)
                return sequence {
                    yieldAll(deck.drop(n))
                    yieldAll(cut)
                }
            }

            override fun toLCF(m: BigInteger) = LCF(1L, -original.toLong(), m)
        }

        data class Deal(val n: Int) : ShuffleTechnique() {

            override fun shuffle(deck: Deck): Deck {
                val old = deck.toList()
                val new = old.toMutableList()
                old.forEachIndexed { i, e ->
                    new[i * n % old.size] = e
                }
                return new.asSequence()
            }

            override fun toLCF(m: BigInteger) = LCF(n.toLong(), 0, m)

        }

        companion object {
            private val dealRegex = """deal with increment ([\d]+)""".toRegex()
            private val cutRegex = """cut ([-\d]+)""".toRegex()

            fun parse(s: String, size: Int): ShuffleTechnique {
                val dealMatch = dealRegex.matchEntire(s)
                val cutMatch = cutRegex.matchEntire(s)
                return if (dealMatch != null) {
                    val n = dealMatch.groupValues[1].toInt()
                    Deal(n)
                } else if (cutMatch != null) {
                    val n = cutMatch.groupValues[1].toInt()
                    if (n > 0) {
                        Cut(n, n)
                    } else {
                        Cut(size + n, n)
                    }
                } else if (s == "deal into new stack") {
                    DealNew
                } else {
                    throw IllegalArgumentException(s)
                }
            }
        }
    }

    fun parse(lines: Sequence<String>, size: Int): Sequence<ShuffleTechnique> {
        return lines.map { ShuffleTechnique.parse(it, size) }
    }

    fun apply(deck: Deck, techniques: Sequence<ShuffleTechnique>): Deck {
        return techniques.fold(deck) { d, t ->
            t.shuffle(d)
        }
    }

    private fun Sequence<ShuffleTechnique>.toLCF(m: BigInteger): LCF {
        return this.map { it.toLCF(m) }.fold(LCF.identity(m)) { acc, lcf -> acc andThen lcf }
    }

    fun solve1(lines: Sequence<String>): Int {
        val end = 10006
        val techniques = parse(lines, end + 1)
        val deck = (0..end).asSequence()
        val after = apply(deck, techniques)
        return after.indexOf(2019)
    }


    fun solve1Bis(lines: Sequence<String>): Int {
        val size = 10007
        val techniques = parse(lines, size)
        val lcf = techniques.toLCF(size.toBigInteger())
        return lcf.apply(ModInt(2019.toBigInteger(), size.toBigInteger())).value.toInt()
    }

    fun solve2(lines: Sequence<String>): Long {
        val size = 119315717514047.toBigInteger()
        val techniques = parse(lines, 0)
        val lcf = techniques.toLCF(size)
        val k = 101741582076661.toBigInteger()
        val multiLcf = lcf.applyK(k)
        val y = ModInt(2020.toBigInteger(), size)
        return multiLcf.reverseApply(y).value.toLong()
    }
}