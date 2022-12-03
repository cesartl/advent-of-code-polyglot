package com.ctl.aoc.kotlin.y2022

object Day3 {

    fun Char.priority(): Int {
        return if (this in ('a'..'z')) {
            this - 'a' + 1
        } else {
            this - 'A' + 27
        }
    }

    fun solve1(input: Sequence<String>): Int {
        return input
            .map { line ->
                val mid = line.length / 2
                val a = line.take(mid)
                val b = line.drop(mid)
                a.toSet().intersect(b.toSet()).first()
            }
            .map { it.priority() }
            .sum()
    }

    fun solve2(input: Sequence<String>): Int {
        return input
            .chunked(3)
            .map { group ->
                group.fold(ALL_LETTERS.toSet()) { acc, line ->
                    acc.intersect(line.toSet())
                }.first()
            }
            .map { it.priority() }
            .sum()
    }
}

private val ALL_LETTERS = ('a'..'z') + ('A'..'Z')
