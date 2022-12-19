package com.ctl.aoc.kotlin.utils

val <T> List<T>.tail: List<T>
    get() = drop(1)

val <T> List<T>.head: T
    get() = first()


fun <T> List<T>.frequency() = Lists.frequency(this)


fun <T> List<T>.mostFrequent(): T? {
    val sorted = this.frequency()
            .toList()
            .sortedBy { -it.second }
    if (sorted.size > 1) {
        if (sorted.first().second == sorted.drop(1).first().second) {
            return null;
        }
    }
    return sorted
            .first()
            .first
}

fun <T> List<T>.leastFrequent(): T? {
    val sorted = this.frequency()
            .toList()
            .sortedBy { it.second }
    if (sorted.size > 1) {
        if (sorted.first().second == sorted.drop(1).first().second) {
            return null;
        }
    }
    return sorted
            .first()
            .first
}

/**
 * Returns all the {collection.size}! endomorphisms from the given collection onto itself
 */
fun <T> Collection<T>.allMappings(): Sequence<Map<T, T>> = Lists.allMappings(this)

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

    /**
     * Returns all the {collection.size}! endomorphisms from the given collection onto itself
     */
    fun <T> allMappings(collection: Collection<T>): Sequence<Map<T, T>> {
        fun go(current: Collection<T>, remaining: Collection<T>): Sequence<Map<T, T>> = sequence {
            if(current.size == 1){
                yield(mapOf(current.first() to remaining.first()))
            }
            if (current.isNotEmpty()) {
                val first = current.first()
                remaining.forEach { x ->
                    go(current.drop(1), remaining - x).forEach { r ->
                        yield(mapOf(first to x) + r)
                    }
                }
            }
        }
        return go(collection, collection)
    }

    fun <T> frequency(list: List<T>): Map<T, Int> {
        val map = mutableMapOf<T, Int>()
        list.forEach {
            map[it] = (map[it] ?: 0) + 1
        }
        return map
    }

    fun List<Int>.median(): Int {
        val numArray = this.sorted()
        return if (numArray.size % 2 == 0) {
            (numArray[numArray.size / 2] + numArray[(numArray.size / 2) - 1]) / 2
        } else {
            numArray[(numArray.size) / 2]
        }
    }

    fun List<Long>.median(): Long {
        val numArray = this.sorted()
        return if (numArray.size % 2 == 0) {
            (numArray[numArray.size / 2] + numArray[(numArray.size / 2) - 1]) / 2
        } else {
            numArray[(numArray.size) / 2]
        }
    }

}
