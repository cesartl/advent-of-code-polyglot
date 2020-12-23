package com.ctl.aoc.kotlin.y2020

object Day23 {

    fun solve1(input: String, n: Int = 100): String {
        val cups = Cups(input.map { it.toString().toInt() }.toIntArray())
        (0 until n).forEach {
            cups.next()
        }
        val l = cups.cups.toList()
        return (l + l).dropWhile { it != 1 }.drop(1).take(l.size - 1).joinToString("")
    }

    fun solve2(input: String): Int {
        TODO()
    }

    data class Cup(val value: Int, val index: Int)

    data class Cups(var cups: IntArray) {
        var currentCupIdx = 0
        fun next() {
            val selected = (0..3).map { (it + currentCupIdx) % cups.size }
                    .map { Cup(cups[it], it) }
            val destination = findDestination(cups[currentCupIdx] - 1, selected.map { it.value })
            val list = cups.toList()
            if (destination.index > currentCupIdx) {
                val newList = list.subList(0, currentCupIdx + 1) +
                        list.subList(currentCupIdx + 4, destination.index + 1) +
                        list.subList(currentCupIdx + 1, currentCupIdx + 4) +
                        list.subList(destination.index + 1, list.size)
                currentCupIdx = (currentCupIdx + 1) % cups.size
                cups = (newList.subList(currentCupIdx, newList.size) + newList.subList(0, currentCupIdx)).toIntArray()
                currentCupIdx = 0
            } else {
                error("")
            }

        }

        tailrec fun findDestination(target: Int, forbidden: List<Int>): Cup {
            if (target <= 0) {
                return findDestination(9, forbidden)
            }
            if (forbidden.contains(target)) {
                return findDestination(target - 1, forbidden)
            }
            val idx = cups.indexOf(target)
            if (idx == -1) {
                return findDestination(target - 1, forbidden)
            }
            return Cup(cups[idx], idx)
        }
    }
}