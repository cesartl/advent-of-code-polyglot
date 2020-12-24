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

    fun solve1bis(input: String, n: Int = 100): String {
        val numbers = input.map { it.toString().toInt() }
        val nextMap = IntArray(10)
        numbers.zipWithNext().forEach { (i, next) ->
            nextMap[i] = next
        }
        nextMap[numbers.last()] = numbers.first()
        play(numbers.first(), n, nextMap, 9)
        return generateSequence(nextMap[1]){i -> nextMap[i]}.take(numbers.size -1).joinToString("")
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

    fun solve2bis(input: String): Long {
        val numbers = input.map { it.toString().toInt() }
        val nextMap = IntArray(1000001)
        (numbers + generateSequence(numbers.max()!! + 1) { it + 1 }.take(1000000 - numbers.size).toList()).zipWithNext().forEach { (i, next) ->
            nextMap[i] = next
        }
        nextMap[1000000] = numbers.first()
        val t = System.currentTimeMillis()
        play(numbers.first(),10000000, nextMap, 1000000)
        println("Took ${System.currentTimeMillis() - t}")
        val n1 = nextMap[1]
        val n2 = nextMap[n1]
        return n1.toLong() * n2.toLong()
    }

    private fun play(start: Int, n: Int, nextMap: IntArray, max: Int) {
        var current = start
        var firstRemove: Int
        var secondRemove: Int
        var thirdRemove: Int
        var target: Int
        (0 until n).forEach { _ ->
            firstRemove = nextMap[current]
            secondRemove = nextMap[firstRemove]
            thirdRemove = nextMap[secondRemove]
            target = if (current == 1) max else current - 1
            while (target == firstRemove || target == secondRemove || target == thirdRemove) {
                target--
                if (target <= 0) {
                    target = max
                }
            }
            nextMap[current] = nextMap[thirdRemove]
            nextMap[thirdRemove] = nextMap[target]
            nextMap[target] = firstRemove
            current = nextMap[current]
        }
    }

    private fun play(n: Int, circular: CircularLinkedList<Int>, max: Int): Pair<CircularLinkedList<Int>, Array<CircularLinkedList<Int>?>> {
        var current = circular
        val cache = Array<CircularLinkedList<Int>?>(max + 1){null}

        var toCache = current
        (0 until max).forEach {
            cache[toCache.value] = toCache
            toCache = toCache.nextNode()
        }
        (0 until n).forEach { i ->
            if (i % 1000000 == 0) {
                println("i: $i")
            }

            val firstToMove = current.nextNode()
            val lastToMove = current.nextNode(3)


            current.next = lastToMove.next
            lastToMove.next!!.previous = current

            var target = if (current.value == 1) max else (current.value - 1)
            while (target == firstToMove.value || target == firstToMove.nextNode().value || target == firstToMove.nextNode(2).value) {
                target--
                if (target <= 0) {
                    target = max
                }
            }

            val toInsert = cache[target]!!
            val toInsertNext = toInsert.nextNode()
            toInsert.next = firstToMove
//            firstToMove.previous = toInsert

            lastToMove.next = toInsertNext
//            toInsertNext.previous = lastToMove
            current = current.nextNode()
        }
        return current to cache
    }
}