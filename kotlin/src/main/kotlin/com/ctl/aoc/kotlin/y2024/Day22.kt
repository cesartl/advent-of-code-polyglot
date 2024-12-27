package com.ctl.aoc.kotlin.y2024

object Day22 {
    fun solve1(input: Sequence<String>): Long {
        return input.map { it.toLong() }
            .sumOf { secret(it, 2000) }
    }

    fun solve2(input: Sequence<String>): Int {
        val all = mutableMapOf<String, Int>()

        input.map { it.toLong() }
            .flatMap { buildKeys(it) }
            .forEach { (key, price) ->
                all[key] = all.getOrDefault(key, 0) + price
            }


        return all.maxOf { it.value }
    }

    private fun allBananas(maps: List<Map<String, Int>>, candidate: String) : Int {
        return maps.asSequence()
            .map { it[candidate] ?: 0 }
            .sum()
    }


    private fun buildKeys(secret: Long): Sequence<Pair<String, Int>> {
        val prices = generateSequence(secret) { nextSecret(it) }
            .take(2001)
            .map { it % 10 }
            .map { it.toInt() }
            .toList()

        return prices
            .asSequence()
            .zipWithNext { a, b -> b - a }
            .windowed(size = 4, step = 1)
            .withIndex()
            .map { (index, window) ->
                val key = window.joinToString(",")
                val price = prices[index + window.size]
                key to price
            }
            .distinctBy { it.first }
    }

    private fun secret(start: Long, n: Int): Long {
        return generateSequence(start) { nextSecret(it) }
            .drop(n)
            .first()
    }


    private fun nextSecret(secret: Long): Long {
        val a = ((secret * 64) % MOD).mixAndPrune(secret)
        val b = (a / 32).mixAndPrune(a)
        val c = ((b * 2048) % MOD).mixAndPrune(b)
        return c
    }
}

private const val MOD = 16777216L

private fun Long.mixAndPrune(secret: Long): Long {
    return (this xor secret) % MOD
}
