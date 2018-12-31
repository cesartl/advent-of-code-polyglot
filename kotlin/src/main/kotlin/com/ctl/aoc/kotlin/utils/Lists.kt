package com.ctl.aoc.kotlin.utils

val <T> List<T>.tail: List<T>
    get() = drop(1)

val <T> List<T>.head: T
    get() = first()

object Lists {
    fun <T> weave(first: List<T>, second: List<T>, prefix: List<T> = listOf()): List<List<T>> {
        if (first.isEmpty()) {
            return listOf(prefix + second)
        }
        if (second.isEmpty()) {
            return listOf(prefix + first)
        }
        return weave(first.tail, second, prefix + first.head) + weave(first, second.tail, prefix + second.head)
    }

    fun <T> powerSet(list: List<T>): List<List<T>> {
        if (list.size > Int.SIZE_BITS) {
            throw IllegalArgumentException("power set too big")
        }
        val total = 1 shl list.size // 2^ n
        val powerSet = mutableListOf<List<T>>()
        for (i in 0 until total) {
            powerSet.add(intToSet(i, list))
        }
        return powerSet
    }

    private fun <T> intToSet(x: Int, list: List<T>): List<T> {
        var k = x
        var idx = 0
        val result = mutableListOf<T>()
        while (k > 0) {
            if (k and 1 == 1) {
                result.add(list[idx])
            }
            k = k shr 1
            idx++
        }
        return result
    }

    fun <T> permutations(list: List<T>): Sequence<List<T>> {
        return sequence {
            if (list.size == 1) yield(list)
            else {
                permutations(list.drop(1)).forEach { permutation ->
                    for (i in 0..permutation.size) {
                        yield(permutation.subList(0, i) + list.first() + permutation.drop(i))
                    }
                }
            }
        }
    }
}