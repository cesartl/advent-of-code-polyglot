package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.frequency

data class Hand(
    val value: String,
    val bidAmount: Long
) {
    val elements = value.toList().frequency()

    private fun sameCard(n: Int, c: Int = 1): Boolean = elements.count { (_, v) -> v == n } >= c
    private fun sameCardWithJoker(n: Int): Boolean {
        val jokers = elements['J'] ?: 0
        return elements.any { (k, v) ->
            if (k == 'J') {
                v == n
            } else {
                v + jokers == n
            }
        }
    }

    private fun isFullHouseJoker(): Boolean {
        val jokers = elements['J'] ?: 0
        val (threes, v) = elements.toList().first { (_, v) ->
            v + jokers == 3
        }
        return elements
            .asSequence()
            .filter { (k, _) -> k != threes && k != 'J' }
            .any { (_, v) -> v == 2 }
    }

    private fun hasTwoPairJoker(): Boolean {
        val jokers = elements['J'] ?: 0
        val (twos, v) = elements.toList().first { (k, v) ->
            v + jokers == 2
        }
        return elements.asSequence()
            .filter { (k, _) -> k != twos && k != 'J' }
            .any { (_, v) -> v == 2 }
    }

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

    val kindJoker: Kind by lazy {
        if (sameCardWithJoker(5)) {
            Kind.FiveOfAKind
        } else if (sameCardWithJoker(4)) {
            Kind.FourOfAKind
        } else if (sameCardWithJoker(3)) {
            if (isFullHouseJoker()) {
                Kind.FullHouse
            } else {
                Kind.ThreeOfAKind
            }
        } else if (sameCardWithJoker(2)) {
            if (hasTwoPairJoker()) {
                Kind.TwoPair
            } else {
                Kind.OnePair
            }
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
private val kindJokerComparator: Comparator<Hand> = compareBy { it.kindJoker }

private val cards = "AKQJT98765432".reversed()
    .withIndex()
    .associate { (index, char) -> char to index }

private val cardsJokers = "AKQT98765432J".reversed()
    .withIndex()
    .associate { (index, char) -> char to index }

private fun compareCard(i: Int, cards: Map<Char, Int>): Comparator<Hand> = compareBy { cards[it.value[i]] }

private val handComparator = (0..<5).fold(kindComparator) { acc, i -> acc.thenComparing(compareCard(i, cards)) }
private val handJokerComparator =
    (0..<5).fold(kindJokerComparator) { acc, i -> acc.thenComparing(compareCard(i, cardsJokers)) }

object Day7 {
    fun solve1(input: Sequence<String>): Long {
        return input.map { it.parseHand() }
            .sortedWith(handComparator)
            .withIndex()
            .sumOf { (index, value) -> (index + 1) * value.bidAmount }
    }

    fun solve2(input: Sequence<String>): Long {
        val x = input.map { it.parseHand() }
            .sortedWith(handJokerComparator)
            .map { it.value to it.kindJoker }
            .toList()
        return input.map { it.parseHand() }
            .sortedWith(handJokerComparator)
            .withIndex()
            .sumOf { (index, value) -> (index + 1) * value.bidAmount }
    }
}


fun String.parseHand(): Hand {
    val parts = this.split(" ")
    return Hand(parts[0], parts[1].toLong())
}
