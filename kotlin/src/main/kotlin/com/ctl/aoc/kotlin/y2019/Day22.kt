package com.ctl.aoc.kotlin.y2019

import java.util.*

typealias Deck = Sequence<Int>

object Day22 {

    sealed class ShuffleTechnique {
        abstract fun shuffle(deck: Deck): Deck

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
        }

        data class Cut(val n: Int) : ShuffleTechnique() {
            override fun shuffle(deck: Deck): Deck {
                val cut = deck.take(n)
                return sequence {
                    yieldAll(deck.drop(n))
                    yieldAll(cut)
                }
            }
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
                        Cut(n)
                    } else {
                        Cut(size + n)
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

    fun solve1(lines: Sequence<String>): Int {
        val end = 10006
        val techniques = parse(lines, end + 1)
        val deck = (0..end).asSequence()
        val after = apply(deck, techniques)
        println(after.toList()[2019])
        val after2 = apply(after, techniques)
        println(after2.toList()[2019])
        return after.indexOf(2019)
    }
}