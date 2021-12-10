package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.Lists.median

object Day10 {

    data class Line(val chars: String) {
        fun corruptedChar(): Char? {
            chars.fold(listOf<Char>()) { openingChars, c ->
                val matchOpen = closeToOpen[c]
                if (matchOpen != null) {
                    val last = openingChars.last()
                    if (matchOpen != last) {
                        return c
                    }
                    openingChars.dropLast(1)
                } else {
                    openingChars + c
                }
            }
            return null
        }

        private fun openingChars(): List<Char> {
            return chars.fold(listOf<Char>()) { openingChars, c ->
                val matchOpen = closeToOpen[c]
                if (matchOpen != null) {
                    val last = openingChars.last()
                    if (matchOpen != last) {
                        error("should not happen")
                    }
                    openingChars.dropLast(1)
                } else {
                    openingChars + c
                }
            }
        }

        fun complete(): List<Char> = openingChars().map { openToClose[it] ?: error("no match for $it") }.reversed()

        fun scoreComplete(): Long {
            val chars = complete()
            return chars.fold(0L) { acc, c ->
                (acc * 5) + (completePoints[c] ?: error("no match for $c "))
            }
        }

        companion object {
            val closeToOpen = mapOf(')' to '(', ']' to '[', '}' to '{', '>' to '<')
            val openToClose = closeToOpen.toList().associate { it.second to it.first }
            val points = mapOf(')' to 3L, ']' to 57L, '}' to 1197L, '>' to 25137L)
            val completePoints = listOf(')', ']', '}', '>').mapIndexed { i, c -> c to (i + 1L) }.toMap()
        }
    }

    fun solve1(input: Sequence<String>): Long {
        return input.map { Line(it) }
                .mapNotNull { it.corruptedChar() }
                .map { Line.points[it] ?: error("no points for $it") }
                .sum()
    }

    fun solve2(input: Sequence<String>): Long {
        return input.map { Line(it) }
                .filter { it.corruptedChar() == null }
                .map { it.scoreComplete() }
                .toList()
                .median()
    }

}