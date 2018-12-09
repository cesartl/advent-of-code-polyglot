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

object Day9p2 {

    data class Node(val value: Long, var next: Node?)

    data class LinkedList(var root: Node?, var size: Int) {
        fun insertAt(idx: Int, value: Long) {
            root = root!!.insertAt(idx, value)
            size++
        }

        fun append(value: Long) {
            insertAt(size, value)
        }

        fun removeAt(idx: Int): Long? {
            val remove = root!!.remove(idx)
            root = remove.first
            size--
            return remove.second
        }

        fun get(idx: Int): Long? = root?.get(idx)?.value

    }


    data class Circle(val marbles: LinkedList, var currentMarbleIdx: Int) {
        fun addMarble(marble: Long): List<Long> {
            return if (marble % 23L != 0L) {
                val i = (currentMarbleIdx + 1) % marbles.size
                marbles.insertAt(i + 1, marble)
                currentMarbleIdx = i + 1
                emptyList()
            } else {
                val idx = (currentMarbleIdx - 7 + marbles.size) % marbles.size
                val removed = marbles.removeAt(idx)
                currentMarbleIdx = idx
                listOf(marble, removed!!)
            }
        }

        fun print() {
            for (i in 0..marbles.size) {
                if (i == currentMarbleIdx) {
                    print(" (${marbles.get(i)}) ")
                } else {
                    print("  ${marbles.get(i)}  ")
                }
            }
            println()
        }
    }


    fun solve(n: Int, lastMarble: Long): Long {
        var circle = Circle(LinkedList(Node(0, null), 1), 0)
        val score = mutableMapOf<Int, List<Long>>()

        var player: Long
        var progress = 0L
        var newProgress = 0L;
        for (marble in 1..lastMarble + 1) {
//            circle.print()
            player = marble % n
            score.merge(player.toInt(), circle.addMarble(marble)) { t, l -> t + l }

            newProgress = (marble * 100) / lastMarble
            if (progress != newProgress) {
                println("$newProgress%")
            }
        }

        val max = score.maxBy { it.value.sum() }!!
        println(max)
        return max.value.sum()
    }

    fun solve2(n: Int, lastMarble: Long): Long {
        var circle = Circle(LinkedList(Node(0, null), 1), 0)
        val score = mutableMapOf<Int, Long>()

        var player: Long
        var progress = 0L
        var newProgress = 0L;
        for (marble in 1..lastMarble + 1) {
//            circle.print()
            player = marble % n
            score.merge(player.toInt(), circle.addMarble(marble).sum()) { t, l -> t + l }

            newProgress = (marble * 100) / lastMarble
            if (progress != newProgress) {
                println("$newProgress%")
            }
            progress = newProgress
        }

        val max = score.maxBy { it.value}!!
        println(max)
        return max.value
    }
}

fun Day9p2.Node.tail(): Day9p2.Node? {
    var current: Day9p2.Node? = this
    while (current?.next != null) {
        current = current.next;
    }
    return current
}

fun Day9p2.Node.append(value: Long): Day9p2.Node {
    val tail = this.tail()
    tail?.next = Day9p2.Node(value, null)
    return this
}

fun Day9p2.Node.prepend(value: Long): Day9p2.Node {
    return Day9p2.Node(value, this)
}

fun Day9p2.Node.get(idx: Int): Day9p2.Node? {
    var i = 0
    var current: Day9p2.Node? = this
    while (current != null && i < idx) {
        i++
        current = current.next
    }
    return current
}

fun Day9p2.Node.insertAt(idx: Int, value: Long): Day9p2.Node {
    if (idx == 0) {
        return prepend(value)
    }
    val after = this.get(idx - 1)
    val node = Day9p2.Node(value, after?.next)
    after?.next = node
    return this
}

fun Day9p2.Node.remove(idx: Int): Pair<Day9p2.Node?, Long?> {
    return if (idx == 0) {
        (this.next to this.value)
    } else {
        val previous = this.get(idx - 1)
        val deletion = previous?.next
        previous?.next = deletion?.next
        this to deletion?.value
    }
}
