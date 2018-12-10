package com.ctl.aoc.kotlin.y2018

object Day9 {

    data class Circle(val marbles: MutableList<Long>, var currentMarbleIdx: Int) {
        fun addMarble(marble: Long): List<Long> {
            return if (marble % 23L != 0L) {
                val i = (currentMarbleIdx + 1) % marbles.size
                marbles.add(i + 1, marble)
                currentMarbleIdx = i + 1
                emptyList()
            } else {
                val idx = (currentMarbleIdx - 7 + marbles.size) % marbles.size
                val removed = marbles.removeAt(idx)
                currentMarbleIdx = idx
                listOf(marble, removed)
            }
        }

        fun print() {
            for (i in marbles.indices) {
                if (i == currentMarbleIdx) {
                    print(" (${marbles[i]}) ")
                } else {
                    print("  ${marbles[i]}  ")
                }
            }
            println()
        }
    }


    fun solve1(n: Int, lastMarble: Long): Long {
        val circle = Circle(mutableListOf(0), 0)
        val score = mutableMapOf<Int, List<Long>>()

        var player: Long
        for (marble in 1..lastMarble) {
            player = marble % n
            score.merge(player.toInt(), circle.addMarble(marble)) { t, l -> t + l }
        }

        val max = score.maxBy { it.value.sum() }!!
        println(max)
        return max.value.sum()
    }

    fun solve2(n: Int, lastMarble: Long): Long {
        val circle = Circle(mutableListOf(0), 0)
        val score = mutableMapOf<Int, Long>()

        var player: Long
        var progress = 0L
        var newProgress = 0L;
        for (marble in 1..lastMarble) {
            player = marble % n
            score.merge(player.toInt(), circle.addMarble(marble).sum()) { t, l -> t + l }
            newProgress = (marble * 100) / lastMarble
            if (progress != newProgress) {
                println("$newProgress%")
            }
            progress = newProgress
        }

        val max = score.maxBy { it.value }!!
        return max.value
    }
}

