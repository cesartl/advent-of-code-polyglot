package com.ctl.aoc.kotlin.utils

object Strings {
    fun permutations(s: String): List<String> {
        if (s.length == 1) return listOf(s)
        val next = permutations(s.substring(1))
        val result = mutableListOf<String>()
        next.forEach {
            for (i in 0..it.length) {
                result.add(it.substring(0, i) + s[0] + it.substring(i))
            }
        }
        return result
    }
}