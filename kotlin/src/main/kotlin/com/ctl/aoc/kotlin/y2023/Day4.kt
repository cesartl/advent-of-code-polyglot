package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.timed
import java.math.BigInteger
import java.util.*

data class ScratchCard(
    val id: Int,
    val winningNumbers: List<Int>,
    val numbers: List<Int>
) {
    val points: Int by lazy {
        val intersect = winningNumbers.toSet().intersect(numbers.toSet())
        when (val n = intersect.size) {
            0 -> {
                0
            }

            1 -> {
                1
            }

            else -> {
                BigInteger.valueOf(2).pow(n - 1).toInt()
            }
        }
    }

    val matches: Int by lazy {
        winningNumbers.toSet().intersect(numbers.toSet()).size
    }
}

private val cardRegex = """Card(\s*)(\d+): """.toRegex()

private fun String.parseScratchCard(): ScratchCard {
    val idMatch = cardRegex.find(this) ?: error("Couldn't find card ID: $this")
    val id = idMatch.groupValues[2].toInt()

    val parts = this.substring(idMatch.range.last)
        .split("|")
        .map { it.trim() }

    val winningNumbers = parts[0]
        .splitToSequence(" ")
        .filter { it.isNotEmpty() }
        .map { it.toInt() }
        .toList()

    val numbers = parts[1]
        .splitToSequence(" ")
        .filter { it.isNotEmpty() }
        .map { it.toInt() }
        .toList()
    return ScratchCard(id, winningNumbers, numbers)
}

object Day4 {
    fun solve1(input: Sequence<String>): Int {
        return input.map { it.parseScratchCard() }
            .sumOf { it.points }
    }

    fun solve2(input: Sequence<String>): Int {
        val dynamic = timed { part2Dynamic(input) }
        val recursive = timed { part2Recursive(input) }
        val queue = timed { part2Queue(input) }
        println("dynamic: $dynamic")
        println("recursive: $recursive")
        println("queue: $queue")
        return dynamic.second
    }

    private fun part2Dynamic(input: Sequence<String>): Int {
        val cards = input.map { it.parseScratchCard() }.toList()
        val copies = mutableMapOf<Int, Int>()
        cards.forEach {
            copies[it.id] = 1
        }
        cards.forEach { card ->
            val n = copies[card.id]!!
            repeat(n) {
                (1..card.matches).forEach { i ->
                    copies.compute(card.id + i) { _, acc ->
                        if (acc != null) {
                            acc + 1
                        } else {
                            0
                        }
                    }
                }
            }
        }
        return copies.values.sum()
    }

    private fun part2Recursive(input: Sequence<String>): Int {
        val cardsById = input
            .map { it.parseScratchCard() }
            .associateBy { it.id }
        val cache = mutableMapOf<Int, Int>()
        return cardsById.values
            .asSequence()
            .map { countCards(it.id, cardsById, cache) }
            .sum()
    }

    private fun part2Queue(input: Sequence<String>): Long {
        val cardsById = input
            .map { it.parseScratchCard() }
            .associateBy { it.id }
        val queue: Deque<ScratchCard> = LinkedList()
        var processed: Long = 0
        cardsById.values.forEach { card ->
            queue.addLast(card)
        }
        while (queue.isNotEmpty()) {
            val card = queue.pop()
            processed++
            repeat(card.matches) {
                cardsById[card.id + it + 1]?.let { nextCard ->
                    queue.addLast(nextCard)
                }
            }
        }
        return processed
    }

    private fun countCards(id: Int, cardsById: Map<Int, ScratchCard>, cache: MutableMap<Int, Int>): Int {
        val card = cardsById[id] ?: return 0
        if (cache.containsKey(id)) {
            return cache[id]!!
        }
        val points = card.matches
        if (points == 0) {
            val result = 1
            cache[id] = result
            return result
        }
        val nextCards = (1..points).sumOf {
            countCards(card.id + it, cardsById, cache)
        }
        val result = 1 + nextCards
        cache[id] = result
        return result
    }
}
