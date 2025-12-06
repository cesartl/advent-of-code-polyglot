package com.ctl.aoc.kotlin.y2025

object Day5 {

    data class Ingredients(
        val freshRanges: List<LongRange>,
        val ingredients: List<Long>
    )

    private fun parseIngredients(input: String): Ingredients {
        val (top, bottom) = input.split("\n\n")
        val freshRanges = top.trim()
            .lineSequence()
            .map { line ->
                val (start, end) = line.split("-")
                start.toLong()..end.toLong()
            }
            .toList()
        val ingredients = bottom.trim().lineSequence().map { it.toLong() }.toList()
        return Ingredients(freshRanges, ingredients)
    }

    fun solve1(input: String): Int {
        val (freshRanges, ingredients) = parseIngredients(input)
        return ingredients.count { ingredient -> freshRanges.any { freshRange -> freshRange.contains(ingredient) } }
    }

    sealed class Edge {
        abstract val value: Long

        data class Start(override val value: Long) : Edge()
        data class End(override val value: Long) : Edge()
    }

    fun Edge.priority(): Int = when (this) {
        is Edge.End -> 1
        is Edge.Start -> 0
    }

    fun solve2(input: String): Long {
        val (freshRanges, _) = parseIngredients(input)
        val comparator = compareBy<Edge> { it.value }.thenBy { it.priority() }
        val edges = freshRanges.asSequence()
            .flatMap { listOf(Edge.Start(it.first), Edge.End(it.last)) }
            .sortedWith(comparator)
            .toList()

        var total = 0L
        var openCount = 0
        var start = 0L
        edges.forEach { edge ->
            when (edge) {
                is Edge.Start -> {
                    if (openCount == 0) {
                        start = edge.value
                    }
                    openCount++
                }

                is Edge.End -> {
                    openCount--
                    if (openCount == 0) {
                        total += edge.value - start + 1
                    }
                }
            }
        }
        return total
    }
}
