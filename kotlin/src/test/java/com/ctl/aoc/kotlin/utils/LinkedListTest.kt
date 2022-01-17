package com.ctl.aoc.kotlin.utils

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class LinkedListTest {
    @Test
    internal fun testFold() {
        val l = listOf(1, 2, 3).toLinkedList()
        println(l)
        assertTrue(l.any { it == 2 })
        assertFalse(l.any { it == 4 })
        assertTrue(l.none { it == 4 })
        assertFalse(l.none { it == 2 })
        assertTrue(l.all { it < 4 })
        assertFalse(l.all { it < 3 })

        println(l.foldLeftIndexed(listOf<String>()) { i, acc, l ->
            acc + "(idx: $i, element: $l)"
        })
    }

    @Test
    internal fun testLazy() {
        val l: LinkedList<() -> Int> = listOf({ 1 }, { 2 }, { 3 }, { error("error") }).toLinkedList()
        assertTrue(l.any { it() == 2 })
    }

    @Test
    internal fun folds() {
        val l = listOf(1, 2, 3, 4).toLinkedList()

        println(l.foldRight(listOf(0)) { n, acc -> acc + (acc.last() + n) })
        println(l.foldLeft(listOf(0)) { acc, n -> acc + (acc.last() + n) })

    }
}