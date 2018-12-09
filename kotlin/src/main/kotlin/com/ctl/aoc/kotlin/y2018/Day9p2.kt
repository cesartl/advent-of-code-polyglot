package com.ctl.aoc.kotlin.y2018

object Day9p2Bis {
    data class Node(val value: Long, var next: Node? = null, var prev: Node? = null) {
        fun insert(node: Node) {
            val next = this.next
            this.next = node
            node.prev = this
            node.next = next
            next?.prev = node
        }

        fun removeNext(): Node? {
            val next = this.next
            this.next = next!!.next
            this.next!!.prev = this
            return next
        }
    }

    fun newNode(value: Long): Node {
        val node = Node(value)
        node.prev = node
        node.next = node
        return node
    }

    fun solve(n: Int, lastMarble: Long): Long {
        val score = mutableMapOf<Int, Long>()
        var player: Long
        var current = newNode(0)
        var removed: Node
        var newN: Node
        for (marble in 1..lastMarble) {
            player = marble % n
            if (marble % 23L != 0L) {
                current = current.next!!
                newN = newNode(marble)
                current.insert(newN)
                current =  newN
            } else {
                for (i in 0..7) {
                    current = current.prev!!
                }
                removed = current.removeNext()!!
                current = current.next!!
                score.merge(player.toInt(), marble + removed.value) { t, l -> t + l }
            }
        }
        return score.maxBy { it.value }?.value ?: 0
    }

}