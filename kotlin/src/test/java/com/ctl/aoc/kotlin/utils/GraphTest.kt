package com.ctl.aoc.kotlin.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GraphTest{
    @Test
    fun testTraversal() {
        val g = Graph<Int>()
        g.addDirectedEdge(1, 2)
        g.addDirectedEdge(2, 3)
        g.addDirectedEdge(3, 4)

        g.addDirectedEdge(1, 5)
        g.addDirectedEdge(5, 7)
        g.addDirectedEdge(7, 8)

        g.addDirectedEdge(1, 6)
        g.addDirectedEdge(6, 9)
        g.addDirectedEdge(9, 10)

        println(g)

//        println("dfs" + g.dfs(1).toList())
//        println("bfs" + g.bfs(1).toList())


        val dfs = g.dfs(1).takeWhile { it != 10 }.toList()
        assertEquals(listOf(1, 6, 9), dfs)

        val bfs = g.bfs(1).takeWhile { it != 10 }.toList()
        assertEquals(listOf(1, 2, 5, 6, 3, 7, 9, 4, 8), bfs)
    }

    @Test
    internal fun Dijkstra() {
        val g = Graph<Int>()
        g.addEdge(1, 2)
        g.addEdge(2, 3)
        g.addEdge(3, 4)
        g.addEdge(4, 5)

        g.addEdge(1, 10)
        g.addEdge(10, 5)

        g.addEdge(1, 6)
        g.addEdge(6, 7)
        g.addEdge(7, 8)
        g.addEdge(7, 10)
        g.addEdge(8, 9)
        g.addEdge(9, 5)

        val result = g.dijkstra(1, 5)

        println(result.findPath(5))


        val result2 = g.dijkstra(1, null)
        println(result.findPath(5))
    }

}