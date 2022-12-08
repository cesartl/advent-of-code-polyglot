package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.Position
import com.ctl.aoc.kotlin.utils.takeWhileInclusive

object Day8 {

    data class Tree(val position: Position, val h: Int)

    fun solve1(input: Sequence<String>): Int {
        val trees = parseTrees(input)

        val maxX = trees.keys.maxOfOrNull { it.x }!!
        val maxY = trees.keys.maxOfOrNull { it.y }!!

        val visibleTrees = mutableSetOf<Tree>()

        for (x in (0..maxX)) {
            visibleTrees.add(trees[Position(x, 0)]!!)
            var maxH = trees[Position(x, 0)]!!.h
            for (y in (1 until maxY)) {
                val current = trees[Position(x, y)]!!
                if (current.h > maxH) {
                    maxH = current.h
                    visibleTrees.add(current)
                }
            }

            visibleTrees.add(trees[Position(x, maxY)]!!)
            maxH = trees[Position(x, maxY)]!!.h
            for (y in (maxY - 1 downTo 1)) {
                val current = trees[Position(x, y)]!!
                if (current.h > maxH) {
                    maxH = current.h
                    visibleTrees.add(current)
                }
            }
        }

        for (y in (0..maxY)) {
            visibleTrees.add(trees[Position(0, y)]!!)
            var maxH = trees[Position(0, y)]!!.h
            for (x in (1 until maxX)) {
                val current = trees[Position(x, y)]!!
                if (current.h > maxH) {
                    maxH = current.h
                    visibleTrees.add(current)
                }
            }

            visibleTrees.add(trees[Position(maxY, y)]!!)
            maxH = trees[Position(maxX, y)]!!.h
            for (x in (maxX - 1 downTo 1)) {
                val current = trees[Position(x, y)]!!
                if (current.h > maxH) {
                    maxH = current.h
                    visibleTrees.add(current)
                }
            }
        }
        println(visibleTrees)
        return visibleTrees.size
    }

    private fun parseTrees(input: Sequence<String>): Map<Position, Tree> {
        val trees = input
            .withIndex()
            .flatMap { (y, line) ->
                line.splitToSequence("")
                    .filter { it.isNotBlank() }
                    .withIndex()
                    .map { (x, h) -> Tree(Position(x, y), h.toInt()) }
            }.map { it.position to it }
            .toMap()
        return trees
    }

    fun Position.isValid(maxX: Int, maxY: Int): Boolean {
        return this.x in (0..maxX) && this.y in (0..maxY)
    }

    fun Tree.directionalScore(
        trees: Map<Position, Tree>,
        maxX: Int,
        maxY: Int,
        offset: Position
    ): Int {
        return generateSequence(this.position) { it + offset }
            .drop(1)
            .takeWhile { it.isValid(maxX, maxY) }
            .takeWhileInclusive { trees[it]!!.h < this.h }
            .count()
    }

    fun Tree.score(trees: Map<Position, Tree>): Int {
        val maxX = trees.keys.maxOfOrNull { it.x }!!
        val maxY = trees.keys.maxOfOrNull { it.y }!!

        return sequenceOf(Position(1, 0), Position(-1, 0), Position(0, 1), Position(0, -1))
            .map { this.directionalScore(trees, maxX, maxY, it) }
            .fold(1) { acc, i -> acc * i }
    }

    fun solve2(input: Sequence<String>): Int {
        val trees = parseTrees(input)
        return trees.values.maxOfOrNull { tree ->
            tree.score(trees)
        } ?: error("Not found")
    }
}
