package com.ctl.aoc.kotlin.`fun`

import java.util.*

object Hanoi {

    private fun <T> move(n: Int, origin: Deque<T>, destination: Deque<T>, buffer: Deque<T>) {
        if (n > 0) {
            move(n - 1, origin, buffer, destination) //move n-1 to buffer using destination as buffer
            val bottom = origin.pop()!! // remove bottom from origin
            destination.push(bottom) // add bottom to destination
            move(n - 1, buffer, destination, origin) // move n-1 to destination using origin as buffer
        }
    }

    fun <T> hanoi(origin: Deque<T>, destination: Deque<T>) {
        val buffer = ArrayDeque<T>()
        move(origin.size, origin, destination, buffer)
    }

    fun towerOfHanoi(n: Int): Deque<Int> {
        val deque = ArrayDeque<Int>()
        (n downTo 1).forEach { deque.push(it) }
        return deque
    }

}