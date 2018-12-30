package com.ctl.aoc.kotlin.y2016

import com.ctl.aoc.kotlin.utils.CircularLinkedList

object Day19 {
    fun josephus(n: Int): Int {
        val p = Integer.highestOneBit(n)
        val l = n - p
        return 2 * l + 1
    }

    fun part2(n: Int): Int {

        var start = CircularLinkedList.of(1)
        var circular = start
        for (i in 2..n) {
            circular = circular.insert(i)
        }
        var size = n

        var current = start
        while (size > 1) {
            current.nextNode(size / 2 - 1).removeNext()
            current = current.nextNode()
            size--
        }
        return current.value
    }

    fun lowPowerOf(n: Int, p: Int): Int {
        var r = 1
        var current = n
        while (current / p > 0) {
            current /= p
            r *= p
        }
        return r
    }

    fun part2bis(n: Int): Int {
        val p = lowPowerOf(n, 3)
        if(p == n) return n
        val remaining = n - p
        return if(remaining <= p){
            remaining
        }else{
            p + (remaining - p) * 2
        }
    }
}