package com.ctl.aoc.kotlin.y2020

object Day2 {

    data class Policy(val char: Char, val range: IntRange) {
        fun matches(password: String): Boolean {
            val freq = password.fold(mutableMapOf<Char, Int>()) { acc, c ->
                acc[c] = (acc[c] ?: 0) + 1
                acc
            }
            return range.contains(freq[char] ?: 0)
        }
        fun matches2(password: String): Boolean {
            return (password[range.first -1] == char) xor (password[range.last -1] == char)
        }
    }

    data class Entry(val policy: Policy, val password: String) {
        fun matches(): Boolean {
            return policy.matches(password)
        }
        fun matches2(): Boolean {
            return policy.matches2(password)
        }
    }

    val regex = """([0-9]+)-([0-9]+) ([a-z]): ([\w]+)""".toRegex()

    fun parse(s: String): Entry {
        val match = regex.matchEntire(s)!!
        val groups = match.groupValues
        val range = IntRange(groups[1].toInt(), groups[2].toInt())
        return Entry(Policy(groups[3][0], range), groups[4])
    }

    fun solve1(input: Sequence<String>): Int {
        val entries = input.map { parse(it) }
        return entries.filter { it.matches() }.count()
    }

    fun solve2(input: Sequence<String>): Int {
        val entries = input.map { parse(it) }
        return entries.filter { it.matches2() }.count()
    }


}