package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.Graph

object Day5 {

    data class SortingRule(val from: Int, val to: Int)

    fun solve1(input: String): Int {
        val (rules, pages) = parseInput(input)
        return pages.asSequence()
            .filter { page ->
                val sortedByIndex: Map<Int, Int> =
                    buildOrder(page, rules).withIndex().associate { it.value to it.index }
                isSorted(sortedByIndex, page)
            }
            .map { page ->
                page[page.size / 2]
            }
            .sum()
    }

    private fun isSorted(sortedByIndex: Map<Int, Int>, page: List<Int>): Boolean {
        (0 until page.size - 1).forEach { i ->
            (i + 1 until page.size).forEach { j ->
                if (getIndex(sortedByIndex, page[i]) > getIndex(sortedByIndex, page[j])) {
                    return false
                }
            }
        }
        return true
    }

    private fun buildOrder(page: List<Int>, rules: List<SortingRule>): List<Int> {
        val graph = Graph<Int>()
        val rootNodes = ArrayDeque<Int>()
        val inScope = page.toSet()
        val sorted = mutableListOf<Int>()
        rules.forEach { rule ->
            if (inScope.contains(rule.from) && inScope.contains(rule.to)) {
                graph.addDirectedEdge(rule.from, rule.to)
            }
        }

        page.forEach { node ->
            if (graph.incomingNodes(node).isEmpty()) {
                rootNodes.add(node)
            }
        }

        while (rootNodes.isNotEmpty()) {
            val current = rootNodes.removeFirst()
            sorted.add(current)
            graph.outgoingNodes(current).forEach { to ->
                graph.removeEdge(current, to)
                if (graph.incomingNodes(to).isEmpty()) {
                    rootNodes.add(to)
                }
            }
        }

        return sorted
    }

    private fun getIndex(sortedByIndex: Map<Int, Int>, n: Int): Int {
        return sortedByIndex[n] ?: error("Missing $n")
    }

    fun solve2(input: String): Int {
        val (rules, pages) = parseInput(input)
        return pages.asSequence()
            .mapNotNull { page ->
                val sortedByIndex: Map<Int, Int> =
                    buildOrder(page, rules).withIndex().associate { it.value to it.index }
                if (isSorted(sortedByIndex, page)) {
                    null
                } else {
                    page.sortedWith { a, b ->
                        val ai = getIndex(sortedByIndex, a)
                        val bi = getIndex(sortedByIndex, b)
                        ai.compareTo(bi)
                    }
                }
            }
            .map { page ->
                page[page.size / 2]
            }
            .sum()
    }

    data class PrintingInput(val rules: List<SortingRule>, val pages: List<List<Int>>)

    private fun parseInput(input: String): PrintingInput {
        val parts = input.trim().split("\n\n")
        val rules = parts[0].splitToSequence("\n").map { line ->
            val (from, to) = line.split("|").map { it.toInt() }
            SortingRule(from, to)
        }.toList()
        val pages = parts[1].splitToSequence("\n").map { line ->
            line.splitToSequence(",").map { it.toInt() }.toList()
        }.toList()
        return PrintingInput(rules, pages)
    }
}
