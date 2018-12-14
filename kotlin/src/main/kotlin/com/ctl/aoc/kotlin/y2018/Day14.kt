package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.CircularLinkedList
import java.text.MessageFormat

object Day14 {

    fun digits(n: Int): List<Int> {
        return if (n == 0) listOf()
        else {
            listOf(n % 10) + digits(n / 10)
        }
    }

    data class Recipes(var elf1: CircularLinkedList<Int>, var elf2: CircularLinkedList<Int>, var end: CircularLinkedList<Int>, var size: Int) {
        fun next(): List<Int> {
            val newScore = elf1.value + elf2.value
            val newRecipes =
                    when (newScore) {
                        0 -> listOf(0)
                        else -> digits(newScore).reversed()
                    }

            newRecipes.forEach { r -> end = end.insert(r) }

            elf1 = elf1.nextNode(1 + elf1.value)
            elf2 = elf2.nextNode(1 + elf2.value)

            size += newRecipes.size
            return newRecipes
        }

        fun last(n: Int): List<Int> {
            val l = mutableListOf<Int>()
            var current = end
            for (i in 1..Math.min(size, n)) {
                l.add(current.value)
                current = current.previousNode()
            }
            return l.reversed()
        }

        fun print(): String {
            val start = end.previousNode()
            return start.print { it ->
                when (it) {
                    elf1 -> "(${it.value})"
                    elf2 -> "[${it.value}]"
                    else -> " ${it.value} "
                }
            }
        }
    }


    fun solve1(input: Int, debug: Boolean = false): String {
        var elf1 = CircularLinkedList.of(3)
        var elf2 = elf1.insert(7)
        val r = Recipes(elf1, elf2, elf2, 2)
        while (r.size < input + 10) {
            if (debug) {
                println(r.print())
            }
            r.next()
        }
        if (debug) {
            println(r.print())
        }

        val buffer = mutableListOf<Int>()
        var current = r.end.previousNode(r.size - input - 10)
        for (i in 1..10) {
            buffer.add(current.value)
            current = current.previousNode()
        }
        return buffer.reversed().joinToString(separator = "")
    }

    fun solve2(input: String): Int {
        println("input: $input")
        var elf1 = CircularLinkedList.of(3)
        var elf2 = elf1.insert(7)
        val r = Recipes(elf1, elf2, elf2, 2)
        var last = ""
        while (!last.contains(input)) {
            r.next()
            if (r.size % 1000000 == 0) {
                println(MessageFormat.format("{0}", r.size))
            }
            last = r.last(input.length+1).joinToString(separator = "")
        }
        val offset = if(last.startsWith(input)) 1 else 0
        return r.size - input.length - offset
    }
}