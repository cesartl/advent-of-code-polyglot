package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.CircularLinkedList

object Day9p2Bis {

    fun solve(n: Int, lastMarble: Long): Long {
        val score = mutableMapOf<Int, Long>()
        var player: Long
        var current = CircularLinkedList.of(0L)
        var removed: Long
        for (marble in 1..lastMarble) {
            player = marble % n
            if (marble % 23L != 0L) {
                current = current.nextNode()
                current = current.insert(marble)
            } else {
                current = current.previousNode(8)
                removed = current.removeNext()
                current = current.nextNode()
                score.merge(player.toInt(), marble + removed) { t, l -> t + l }
            }
        }
        return score.maxBy { it.value }?.value ?: 0
    }

}