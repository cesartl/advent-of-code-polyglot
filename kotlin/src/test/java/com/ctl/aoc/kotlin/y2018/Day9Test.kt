package com.ctl.aoc.kotlin.y2018

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day9Test{

    val input = 423 to 71944L

    @Test
    internal fun testPart1() {
        assertEquals(32, Day9.solve1(9, 25))
        assertEquals(8317, Day9.solve1(10, 1618))
        assertEquals(146373, Day9.solve1(13, 7999))
        assertEquals(2764, Day9.solve1(17, 1104))
        assertEquals(54718, Day9.solve1(21, 6111))
        assertEquals(37305, Day9.solve1(30, 5807))

        println(Day9.solve1(input.first, input.second))
    }

    @Test
    internal fun testLinkedList() {
        val head = Day9p2.Node(1, null)
        head.append(2)
        head.append(3).append(4)

        println(head)
        println("get")
//        println(head.insertAt(10, 9))
        println(head.remove(2))
    }

    @Test
    internal fun testPart2() {
        assertEquals(32, Day9p2Bis.solve(9, 25))
        assertEquals(8317,Day9p2Bis.solve(10, 1618))
        assertEquals(146373, Day9p2Bis.solve(13, 7999))
        assertEquals(2764, Day9p2Bis.solve(17, 1104))
        assertEquals(54718, Day9p2Bis.solve(21, 6111))
        assertEquals(37305, Day9p2Bis.solve(30, 5807))

        for (i in 0..7){
            println(i)
        }

        println(Day9p2Bis.solve(input.first, input.second * 100))
    }
}