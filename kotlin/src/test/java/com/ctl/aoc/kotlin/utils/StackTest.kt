package com.ctl.aoc.kotlin.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class StackTest{
    @Test
    internal fun testQueue() {
        val q = Queue<Int>()
        q.push(1)
        q.push(2)
        q.push(3)

        assertEquals(1, q.peek())
        assertEquals(1, q.pop())

        assertEquals(2, q.peek())
        assertEquals(2, q.pop())

        assertEquals(3, q.peek())
        assertEquals(3, q.pop())
    }

    @Test
    internal fun testStack() {
        val s = Stack<Int>()
        s.push(1)
        s.push(2)
        s.push(3)

        assertEquals(3, s.peek())
        assertEquals(3, s.pop())

        assertEquals(2, s.peek())
        assertEquals(2, s.pop())

        assertEquals(1, s.peek())
        assertEquals(1, s.pop())
    }
}