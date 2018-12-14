package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.CircularLinkedList
import java.lang.StringBuilder

object Day14 {

    fun digits(n: Int): List<Int> {
        return if (n == 0) emptyList()
        else {
            listOf(n % 10) + digits(n / 10)
        }
    }

    data class Recicies(var elf1: CircularLinkedList<Int>, var elf2: CircularLinkedList<Int>, var end: CircularLinkedList<Int>, var size: Int) {
        fun next(): List<Int> {
            val newScore = elf1.value + elf2.value
            val newRecipies =
                    when (newScore) {
                        0 -> listOf(0)
                        else -> digits(newScore).reversed()
                    }

            newRecipies.forEach { r -> end = end.insert(r) }

            elf1 = elf1.nextNode(1 + elf1.value)
            elf2 = elf2.nextNode(1 + elf2.value)

            size += newRecipies.size
            return newRecipies
        }

        fun print(): String {
            val builedr = StringBuilder()
//            for (i in end.indices) {
//                when (i) {
//                    idx1 -> builedr.append("(${end[i]})")
//                    idx2 -> builedr.append("[${end[i]}]")
//                    else -> builedr.append(" ${end[i]} ")
//                }
//            }
            return builedr.toString()
        }
    }


    fun solve1(input: Int, debug: Boolean = false): String {
        var elf1 = CircularLinkedList.of(3)
        var elf2 = elf1.insert(7)
        val r = Recicies(elf1, elf2, elf2, 2)
        var next: List<Int> = listOf()
        while (r.size < input + 10) {
            if (debug) {
                println(r.print())
            }
//            println(r.end.size)
            next = r.next()
        }
        if (debug) {
            println(r.print())
        }

//        println(r.end.nextNode().print())

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
        val r = Recicies(elf1, elf2, elf2, 2)
        var str = ""
        var count = 2
        var next: List<Int>
        while (!str.startsWith(input)) {
            next = r.next()
            next.forEach { i ->
                str += i
                if (!input.startsWith(str)) {
                    str = i.toString()
                }
                count += 1
            }
            if(count % 100000 == 0){
                println("count ${Time}")
            }
        }
        println("str: $str count: $count")
        return count - input.length
    }
}