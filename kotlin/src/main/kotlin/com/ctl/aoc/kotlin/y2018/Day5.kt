package com.ctl.aoc.kotlin.y2018

object Day5 {
    fun solve1(line: String): String {
        return reduceString(line)
    }

    fun solve2(polymer: String): String{
        val allTypes = mutableSetOf<Char>()
        allTypes.addAll(polymer.map { it.toLowerCase() }.toCharArray().asSequence())

        val allReduced = allTypes.map { removeTypeAndReduce(it, polymer) }
        return allReduced.minByOrNull { it.length } !!
    }

    fun removeTypeAndReduce(type: Char, polymer: String): String{
        return reduceString(polymer.filter { it.toLowerCase() != type })
    }

    tailrec fun reduceString(s: String): String {
        var reduce = s.fold("") { acc, char ->
            if (acc.isNotEmpty() && react(acc.last(), char)) {
                acc.dropLast(1)
            } else {
                acc + char
            }
        }
        return if (reduce.length == s.length) {
            reduce
        } else {
            reduceString(reduce)
        }
    }

    fun react(c1: Char, c2: Char): Boolean = (c1.toUpperCase() == c2 || c2.toUpperCase() == c1 ) && c1 != c2
}