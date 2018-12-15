package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.CircularLinkedList
import com.ctl.aoc.kotlin.utils.Sequences
import com.ctl.aoc.kotlin.utils.startWith
import java.text.MessageFormat

object Day14 {

    fun digits(n: Byte): List<Byte> {
        return if (n == 0.toByte()) listOf()
        else {
            listOf((n % 10).toByte()) + digits((n / 10).toByte())
        }
    }

    data class Recipes(var elf1: CircularLinkedList<Byte>, var elf2: CircularLinkedList<Byte>, var end: CircularLinkedList<Byte>, var size: Int) {
        fun next(): List<Byte> {
            val newScore: Byte = (elf1.value + elf2.value).toByte()
            val newRecipes =
                    when (newScore) {
                        0.toByte() -> listOf(0.toByte())
                        else -> digits(newScore).reversed()
                    }

            newRecipes.forEach { r -> end = end.insert(r) }

            elf1 = elf1.nextNode(1 + elf1.value)
            elf2 = elf2.nextNode(1 + elf2.value)

            size += newRecipes.size
            return newRecipes
        }

        fun last(n: Int): Sequence<Byte> {
            return sequence {
                var current = end
                for (i in 1..Math.min(size, n)) {
                    yield(current.value)
                    current = current.previousNode()
                }
            }
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
        var elf1 = CircularLinkedList.of(3.toByte())
        var elf2 = elf1.insert(7.toByte())
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

        val buffer = mutableListOf<Byte>()
        var current = r.end.previousNode(r.size - input - 10)
        for (i in 1..10) {
            buffer.add(current.value)
            current = current.previousNode()
        }
        return buffer.reversed().joinToString(separator = "")
    }

    fun solve2(input: String): Int {
        println("input: $input")
        var elf1 = CircularLinkedList.of(3.toByte())
        var elf2 = elf1.insert(7.toByte())
        val r = Recipes(elf1, elf2, elf2, 2)
        var last: Sequence<Byte> = sequenceOf()
        val reverseInput = input.map { (it - '0').toByte() }.reversed().asSequence()
        while (last.none() || !(last.drop(1).startWith(reverseInput) || last.startWith(reverseInput))) {
            r.next()
            if (r.size % 1000000 == 0) {
                println(MessageFormat.format("{0}", r.size))
            }
            last = r.last(input.length + 1)
        }

        val offset = if(last.first() == reverseInput.first()) 0 else 1
        return r.size - input.length - offset
    }
}