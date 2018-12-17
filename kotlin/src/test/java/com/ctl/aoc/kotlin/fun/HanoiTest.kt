package com.ctl.aoc.kotlin.`fun`

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.util.*

internal class HanoiTest {

    @Test
    fun tower() {
        val tower = Hanoi.towerOfHanoi(4)
        assertEquals(1, tower.pop())
        assertEquals(2, tower.pop())
        assertEquals(3, tower.pop())
        assertEquals(4, tower.pop())
        assertTrue(tower.isEmpty())
    }

    @Test
    internal fun hanoi() {
        val dest = ArrayDeque<Int>()
        val n = 31
        Hanoi.hanoi(Hanoi.towerOfHanoi(n), dest)
        for (i in 1..n) {
            println("$i")
            assertEquals(i, dest.pop())
        }
        assertTrue(dest.isEmpty())
    }
}