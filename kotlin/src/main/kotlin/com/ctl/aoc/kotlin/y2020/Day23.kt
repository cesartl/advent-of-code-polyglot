package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.CircularLinkedList

object Day23 {

    fun solve1(input: String, n: Int = 100): String {
        val numbers = input.map { it.toString().toInt() }
        var current = CircularLinkedList.of(numbers.first())
        numbers.drop(1).reversed().forEach { current.insert(it) }
        val max = 9
        current = play(n, current, max)
        while (current.value != 1) {
            current = current.nextNode()
        }
        current = current.nextNode()
        var result = mutableListOf<Int>()
        (0 until numbers.size - 1).forEach {
            result.add(current.value)
            current = current.nextNode()
        }
        return result.joinToString("")
    }

    fun solve2(input: String): Int {
        val numbers = input.map { it.toString().toInt() }
        var current = CircularLinkedList.of(numbers.first())
        numbers.drop(1).reversed().forEach { current.insert(it) }
        val last = current.nextNode(numbers.size - 1)
        generateSequence(1000000) { it - 1 }.take(1000000 - numbers.size).forEach {
            last.insert(it)
        }
        play(10000000, current, 1000000)
        TODO()
    }


    private fun play(n: Int, current: CircularLinkedList<Int>, max: Int): CircularLinkedList<Int> {
        var current1 = current
        var searching = 0L
        var startTime = System.currentTimeMillis()
        (0 until n).forEach { i ->
            if (i % 1000 == 0) {
                println("i: $i, spent searching: $searching ms / ${System.currentTimeMillis() - startTime})")
            }
            //            println(current.print())
            val toMove = listOf(current1.removeNext(), current1.removeNext(), current1.removeNext())
            var target = if (current1.value == 1) max else (current1.value - 1)
            while (toMove.contains(target)) {
                target--
                if (target <= 0) {
                    target = max
                }
            }
            val start = current1
//            println("Looking for $target")
            var count = 0
            searching -= System.currentTimeMillis()
            while (current1.value != target) {
                current1 = current1.previousNode()
                count++
            }
            searching += System.currentTimeMillis()
//            println("Found after $count count")
            toMove.reversed().forEach {
                current1.insert(it)
            }
            current1 = start.nextNode()
        }
        return current1
    }
}