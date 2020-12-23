package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.CircularLinkedList

object Day23 {

    fun solve1(input: String, n: Int = 100): String {
        val numbers = input.map { it.toString().toInt() }
        var current = CircularLinkedList.of(numbers.first())
        numbers.drop(1).reversed().forEach { current.insert(it) }
        val max = 9
        val (finalNode, cache) = play(n, current, max)
        current = cache[1]!!
        current = current.nextNode()
        var result = mutableListOf<Int>()
        (0 until numbers.size - 1).forEach {
            result.add(current.value)
            current = current.nextNode()
        }
        return result.joinToString("")
    }

    fun solve2(input: String): Long {
        val numbers = input.map { it.toString().toInt() }
        var current = CircularLinkedList.of(numbers.first())
        numbers.drop(1).reversed().forEach { current.insert(it) }
        val last = current.nextNode(numbers.size - 1)
        generateSequence(1000000) { it - 1 }.take(1000000 - numbers.size).forEach {
            last.insert(it)
        }
        val (finalNode, cache) = play(10000000, current, 1000000)
        val one = cache[1]!!
        return one.nextNode().value.toLong() * one.nextNode().nextNode().value.toLong()
    }


    private fun play(n: Int, circular: CircularLinkedList<Int>, max: Int): Pair<CircularLinkedList<Int>, MutableMap<Int, CircularLinkedList<Int>>> {
        var current = circular
        var searching = 0L
        var startTime = System.currentTimeMillis()
        var previous: CircularLinkedList<Int>
        var next: CircularLinkedList<Int>
        val cache = mutableMapOf<Int, CircularLinkedList<Int>>()

        var toCache = current
        while (cache.size < max) {
            cache[toCache.value] = toCache
            toCache = toCache.nextNode()
        }
        (0 until n).forEach { i ->
            if (i % 100000 == 0) {
                println("i: $i, spent searching: $searching ms / ${System.currentTimeMillis() - startTime})")
            }
//            println(current.print())
            val toMove = listOf(current.removeNext(), current.removeNext(), current.removeNext())
            toMove.forEach { cache.remove(it) }
            var target = if (current.value == 1) max else (current.value - 1)
            while (toMove.contains(target)) {
                target--
                if (target <= 0) {
                    target = max
                }
            }
//            println("Looking for $target")
            var count = 0
            searching -= System.currentTimeMillis()

//            previous = current
//            next = current
//            while ((previous.value != target) && (next.value != target)) {
//                next = next.nextNode()
//                previous = previous.previousNode()
//                count++
//            }
//            val toInsert = if(previous.value == target) previous else next
            val toInsert = cache[target]!!

            searching += System.currentTimeMillis()

            toMove.reversed().forEach {
                cache[it] = toInsert.insert(it)
            }
            current = current.nextNode()
        }
        return current to cache
    }
}