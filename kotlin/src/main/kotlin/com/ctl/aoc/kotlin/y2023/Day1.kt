package com.ctl.aoc.kotlin.y2023

object Day1 {
    fun solve1(input: Sequence<String>): Int {
        val list = input.map { line ->
            val d = line.asSequence().filter { it.isDigit() }.toList()
            listOf(d.first(), d.last()).joinToString(separator = "").toInt()
        }.toList()
        return list.sum()
    }

    val regex = """(\d|one|two|three|four|five|six|seven|eight|nine)""".toRegex()

    fun solve2(input: Sequence<String>): Int {
        return input.map { line ->
            val parts = line
                .subStrings()
                .mapNotNull { regex.find(it) }
                .map { it.value }
                .map { it.parseValue() }
                .toList()
            listOf(parts.first(), parts.last()).joinToString(separator = "").toInt()
        }.sum()
    }
}

private fun String.parseValue(): Int = when (this) {
    "one" -> 1
    "two" -> 2
    "three" -> 3
    "four" -> 4
    "five" -> 5
    "six" -> 6
    "seven" -> 7
    "eight" -> 8
    "nine" -> 9
    else -> this.toInt()
}

fun String.subStrings(): Sequence<String> {
    return generateSequence(this) { s ->
        s.substring(1)
    }.takeWhile { it.isNotEmpty() }
}
