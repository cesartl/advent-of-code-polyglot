package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.CircularLinkedList

object Day20 {
    fun solve1(input: Sequence<String>): Int {
        val list = input.map { it.toInt() }.toList()
        val size = list.size
        val circular = CircularLinkedList.of(list.first())
        var insert = circular
        val nodes = mutableListOf<CircularLinkedList<Int>>()
        list.drop(1).forEach {
            insert = insert.insert(it)
            nodes.add(insert)
        }
        nodes.add(circular)
        println(circular.print { "${it.value} " })
        var current = circular
        val iterator = nodes.iterator()
        var first = current
        repeat(list.size) {
            if (current == first) {
                first = current.nextNode()
            }
            val n = current.value
            val mod = if (n >= 0) {
                n % size
            } else {
                (2 * size + n - 1) % size
            }
//            println("Doing ${current.value} (mod=$mod)")
            if (mod > 0) {
                current = current.previousNode()
                current.removeNext()
                current = current.nextNode(mod)
                current.insert(n)
            } else if (mod < 0) {
                error("n=$n mod = $mod")
            }
//            println("current: " + current.print { "${it.value} " })
            current = iterator.next()
        }
        println(first.print { "${it.value} " })
        var zero = current
        while (zero.value != 0) {
            zero = zero.nextNode()
        }
        return sequenceOf(1000, 2000, 3000)
            .map { zero.nextNode(it % size).value }
            .sum()
    }

    fun solve2(input: Sequence<String>): Int {
        TODO()
    }
}
