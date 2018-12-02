package com.ctl.aoc.kotlin.utils

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


        val search = g.dfs(1).takeWhile { it != 10 }.toList()
        println(search)
    }

    @Test
    internal fun Dijkstra() {
        val g = Graph<Int>()
        g.addDirectedEdge(1, 2)
        g.addDirectedEdge(2, 3)
        g.addDirectedEdge(3, 4)
        g.addDirectedEdge(4, 5)

        g.addDirectedEdge(1, 10)
        g.addDirectedEdge(10, 5)

        g.addDirectedEdge(1, 6)
        g.addDirectedEdge(6, 7)
        g.addDirectedEdge(7, 8)
        g.addDirectedEdge(7, 10)
        g.addDirectedEdge(8, 9)
        g.addDirectedEdge(9, 5)

        val result = g.dijkstra(1, 5)

        println(result.findPath(5))
    }

}