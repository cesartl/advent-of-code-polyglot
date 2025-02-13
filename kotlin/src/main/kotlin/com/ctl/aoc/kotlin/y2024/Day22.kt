package com.ctl.aoc.kotlin.y2024

object Day22 {
    fun solve1(input: Sequence<String>): Long {
        return input.map { it.toLong() }
            .sumOf { secret(it, 2000) }
    }

    fun solve2(input: Sequence<String>): Int {
        buildMap {
            buildKeys2(123L)
        }
        val all = buildMap {
            input.map { it.toLong() }
                .forEach {
                    buildKeys2(it)
                    println("")
                }
        }
        return all.maxOf { it.value }
    }

    private fun MutableMap<String, Int>.buildKeys(secret: Long) {
        generateSequence(secret) { nextSecret(it) }
            .take(2001)
            .map { it % 10 }
            .map { it.toInt() }
            .windowed(5, 1)
            .map { window ->
                window.zipWithNext { a, b -> b - a } to window.last()
            }
            .distinctBy { it.first }
            .forEach { (window, value) ->
                val key = window.joinToString(separator = ",")
                this[key] = this.getOrDefault(key, 0) + value
            }

    }

    private fun MutableMap<String, Int>.buildKeys2(secret: Long) {
        val window = ArrayDeque<Int>(5)
        generateSequence(secret) { nextSecret(it) }
            .take(2001)
            .map { it % 10 }
            .map { it.toInt() }
            .zipWithNext { a, b -> b to b - a }
            .forEach { (price, diff) ->
                window.add(diff)
                if (window.size == 4) {
                    val key = window.joinToString(",")
                    this[key] = this.getOrDefault(key, 0) + price
                    window.removeFirst()
                }
            }

    }

    private fun secret(start: Long, n: Int): Long {
        return generateSequence(start) { nextSecret(it) }
            .drop(n)
            .first()
    }


    private fun nextSecret(secret: Long): Long {
        val a = (secret * 64).mixAndPrune(secret)
        val b = (a / 32).mixAndPrune(a)
        val c = (b * 2048).mixAndPrune(b)
        return c
    }
}

private const val MOD = 16777216L

private fun Long.mixAndPrune(secret: Long): Long {
    return (this xor secret) % MOD
}
