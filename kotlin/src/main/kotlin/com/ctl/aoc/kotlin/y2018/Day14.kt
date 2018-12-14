package com.ctl.aoc.kotlin.y2018

import java.lang.StringBuilder

object Day14 {

    fun digits(n: Int): List<Int> {
        return if (n == 0) emptyList()
        else {
            listOf(n % 10) + digits(n / 10)
        }
    }

    data class Recicies(var idx1: Int, var idx2: Int, val recipies: MutableList<Int>) {
        fun next(): List<Int> {
            val newScore = recipies[idx1] + recipies[idx2]
            val newRecipies =
                    when (newScore) {
                        0 -> listOf(0)
                        else -> digits(newScore).reversed()
                    }
            recipies.addAll(newRecipies)
            idx1 = (idx1 + 1 + recipies[idx1]) % recipies.size
            idx2 = (idx2 + 1 + recipies[idx2]) % recipies.size
//            println("1: $idx1 2: $idx2 size: ${recipies.size}")
            return newRecipies
        }

        fun print(): String {
            val builedr = StringBuilder()
            for (i in recipies.indices) {
                when (i) {
                    idx1 -> builedr.append("(${recipies[i]})")
                    idx2 -> builedr.append("[${recipies[i]}]")
                    else -> builedr.append(" ${recipies[i]} ")
                }
            }
            return builedr.toString()
        }
    }


    fun solve1(input: Int, debug: Boolean = false): String {
        val r = Recicies(0, 1, mutableListOf())
        r.recipies.add(3)
        r.recipies.add(7)

        while (r.recipies.size < input + 11) {
            if (debug) {
                println(r.print())
            }
//            println(r.recipies.size)
            r.next()
        }
        if (debug) {
            println(r.print())
        }
        return r.recipies.subList(input, input + 10).joinToString(separator = "")
    }

    fun solve2(input: String): Int {
        println("input: $input")
        val r = Recicies(0, 1, mutableListOf())
        r.recipies.add(3)
        r.recipies.add(7)
        var str = ""
        var count = 2
        while (!str.startsWith(input)) {
            val next = r.next()
            next.forEach { i ->
                str += i
                if (!input.startsWith(str)) {
                    str = i.toString()
                }
                count += 1
            }
        }
        println("str: $str count: $count")
        return count - input.length
    }
}