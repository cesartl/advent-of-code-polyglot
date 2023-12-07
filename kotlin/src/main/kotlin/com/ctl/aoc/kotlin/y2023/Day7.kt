package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.frequency

data class Hand(
    val value: String,
    val bidAmount: Long
) {
    val elements = value.toList().frequency()

    private fun sameCard(n: Int, c: Int = 1): Boolean = elements.count { (_, v) -> v == n } >= c

    val kind: Kind by lazy {
        if (sameCard(5)) {
            Kind.FiveOfAKind
        } else if (sameCard(4)) {
            Kind.FourOfAKind
        } else if (sameCard(3)) {
            if (sameCard(2)) {
                Kind.FullHouse
            } else {
                Kind.ThreeOfAKind
            }
        } else if (sameCard(2, 2)) {
            Kind.TwoPair
        } else if (sameCard(2)) {
            Kind.OnePair
        } else {
            Kind.HighCard
        }
    }
}

enum class Kind {
    HighCard,
    OnePair,
    TwoPair,
    ThreeOfAKind,
    FullHouse,
    FourOfAKind,
    FiveOfAKind
}

private val kindComparator: Comparator<Hand> = compareBy { it.kind }

private val cards = "AKQJT98765432".reversed()
    .withIndex()
    .associate { (index, char) -> char to index }

private fun compareCard(i: Int): Comparator<Hand> = compareBy { cards[it.value[i]] }

private val handComparator = (0..<5).fold(kindComparator) { acc, i -> acc.thenComparing(compareCard(i)) }

object Day7 {
    fun solve1(input: Sequence<String>): Long {
        return input.map { it.parseHand() }
            .sortedWith(handComparator)
            .withIndex()
            .sumOf { (index, value) -> (index + 1) * value.bidAmount }
    }

    fun solve2(input: Sequence<String>): Int {
        TODO()
    }
}


private fun String.parseHand(): Hand {
    val parts = this.split(" ")
    return Hand(parts[0], parts[1].toLong())
}
