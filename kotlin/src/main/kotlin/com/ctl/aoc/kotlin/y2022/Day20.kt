package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.CircularLinkedList
import kotlin.math.absoluteValue

object Day20 {
    fun solve1(input: Sequence<String>): Long {
        val list = input.map { it.toLong() }.toList()
        val mixed = mix(list)
        println("Mixed: $mixed")
        return findCoordinates(mixed)
    }

    fun solve2(input: Sequence<String>): Long {
        val encryptionKey = 811589153L
        val list = input.map { it.toLong() * encryptionKey }.toList()
        val mixed = mix(list, 10)
        println("Mixed: $mixed")
        return findCoordinates(mixed)
    }

    private fun findCoordinates(list: List<Long>): Long {
        val i = list.indexOf(0)
        return sequenceOf(1000, 2000, 3000)
            .map { list[(i + it) % list.size] }
            .sum()
    }

    private fun mix(list: List<Long>, nMix: Int = 1): List<Long> {
        val size = list.size
        val circular = CircularLinkedList.of(list.first())
        var insert = circular
        val nodes = mutableListOf<CircularLinkedList<Long>>()
        nodes.add(circular)
        list.drop(1).forEach {
            insert = insert.insert(it)
            nodes.add(insert)
        }
        println(circular.print { "${it.value} " })

        repeat(nMix) {
            var index = 0
            var current = nodes.first()
            var first = current
//            println("new mix")
            repeat(list.size) {
                if (current == first) {
                    first = current.nextNode()
                }
                val n = current.value
//                println("Doing ${current.value}")
                if (n > 0) {
                    current = current.previousNode()
                    current.removeNext()
                    current = current.nextNode((n % (size - 1)).toInt())
                    nodes[index] = current.insert(n)
                } else if (n < 0) {
                    current = current.previousNode()
                    current.removeNext()
                    current = current.previousNode((n.absoluteValue % (size - 1)).toInt())
                    nodes[index] = current.insert(n)
                }
                //            println("current: " + current.print { "${it.value} " })
                //            println("first: "+first.print { "${it.value} " })

                if(index < size-1) {
                    index++
                    current = nodes[index]
                }
            }
        }
//        println(first.print { "${it.value} " })
        var toAdd = nodes.first()
        val mixed = mutableListOf<Long>()
        repeat(size) {
            mixed.add(toAdd.value)
            toAdd = toAdd.nextNode()
        }
        return mixed
    }
}
