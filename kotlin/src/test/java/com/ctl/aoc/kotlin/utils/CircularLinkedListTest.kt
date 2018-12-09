package com.ctl.aoc.kotlin.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class CircularLinkedListTest{
    @Test
    internal fun testCircular() {
        val root = CircularLinkedList.of(0)
        println(root.print())
        println(root.insert(1).print())
        println(root.insert(2).print())
        println(root.insert(3).print())
    }
}