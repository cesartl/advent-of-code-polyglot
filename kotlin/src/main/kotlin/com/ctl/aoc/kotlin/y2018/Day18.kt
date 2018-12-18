package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.y2018.Day18.ForestElement.*
import java.lang.IllegalArgumentException
import java.lang.StringBuilder

object Day18 {

    sealed class ForestElement {
        object Tree : ForestElement() {
            override fun toString(): String = "|"
        }

        object Lumberjack : ForestElement() {
            override fun toString(): String = "#"
        }

        object Open : ForestElement() {
            override fun toString(): String = "."
        }
    }

    fun adjacentsPairs(p: Pair<Int, Int>): Sequence<Pair<Int, Int>> {
        val (x, y) = p
        return sequenceOf(
                (x - 1 to y), //W
                (x to y - 1), // N
                (x - 1 to y - 1), //NW
                (x + 1 to y), // E
                (x to y + 1), // S
                (x + 1 to y + 1), // SE
                (x - 1 to y + 1), //SW
                (x + 1 to y - 1) // NE
        )
    }

    data class Forest(val elements: Map<Int, Map<Int, ForestElement>>) {
        fun print(): String {
            val builder = StringBuilder()
            elements.keys.sorted().forEach { y ->
                val row = elements[y]!!
                row.keys.sorted().forEach { x ->
                    val e = row[x]!!
                    builder.append(e)
                }
                builder.append("\n")
            }
            return builder.toString()
        }

        fun all() = elements.values.flatMap { it.values }

        fun at(p: Pair<Int, Int>): ForestElement? = elements[p.second]?.get(p.first)

        fun adjacents(p: Pair<Int, Int>): Sequence<ForestElement> {
            return adjacentsPairs(p).map { at(it) }.filterNotNull()
        }

        fun next(): Forest {
            val map = mutableMapOf<Int, MutableMap<Int, ForestElement>>()
            elements.keys.sorted().forEach { y ->
                val row = elements[y]!!
                row.keys.sorted().forEach { x ->
                    val element = at(x to y)!!
                    val adjacents = adjacents(x to y)

                    val newElement = when (element) {
                        is Open -> {
                            if (adjacents.filterIsInstance(Tree.javaClass).count() >= 3) {
                                Tree
                            } else {
                                element
                            }
                        }
                        Tree -> {
                            if (adjacents.filterIsInstance(Lumberjack.javaClass).count() >= 3) {
                                Lumberjack
                            } else {
                                element
                            }
                        }
                        Lumberjack -> {
                            val lumberjacks = adjacents.filterIsInstance(Lumberjack.javaClass).count()
                            val trees = adjacents.filterIsInstance(Tree.javaClass).count()
                            if (lumberjacks >= 1 && trees >= 1) {
                                element
                            } else {
                                Open
                            }
                        }
                    }
                    map.computeIfAbsent(y) { mutableMapOf() }[x] = newElement
                }
            }
            return Forest(map)
        }

        fun value(): Int {
            val all = all()
            val trees = all.filterIsInstance(Tree.javaClass).count()
            val lumberjacks = all.filterIsInstance(Lumberjack.javaClass).count()
            return trees * lumberjacks
        }
    }

    fun parse(lines: List<String>): Forest {
        val elements = mutableMapOf<Int, MutableMap<Int, ForestElement>>()
        for (y in lines.indices) {
            val row = lines[y]
            for (x in row.indices) {
                val element: ForestElement = when (row[x]) {
                    '#' -> Lumberjack
                    '|' -> Tree
                    '.' -> Open
                    else -> throw IllegalArgumentException(row[x].toString())
                }
                elements.computeIfAbsent(y) { mutableMapOf() }[x] = element
            }
        }
        return Forest(elements)
    }

    fun solve1(lines: List<String>, n: Long): Int {
        var forest = parse(lines)
        return iterate(forest, n)
    }

    fun solve2(lines: List<String>, n: Long): Int {
        var forest = parse(lines)
        var all: List<ForestElement>
        var trees: Int = 0
        var lumberjacks: Int = 0

        val states = mutableMapOf<Forest, Long>()
        var cycle: Long?
        for (i in 0 until n) {
            cycle = states[forest]
            if (cycle != null) {
                println("cycle found between $cycle and $i")
                val diff = i - cycle
                val rest = n - i
                return iterate(forest, rest % diff)
            } else {
                states[forest] = i
            }

            println("n: $i")
            forest = forest.next()
            all = forest.all()
            trees = all.filterIsInstance(Tree.javaClass).count()
            lumberjacks = all.filterIsInstance(Lumberjack.javaClass).count()
            println("trees: $trees")
            println("lumberjacks: $lumberjacks")
        }
        println(forest.print())
        return trees * lumberjacks
    }

    fun iterate(forest: Forest, n: Long): Int {
        println("iterate $n")
        var all: List<ForestElement>
        var trees: Int = 0
        var lumberjacks: Int = 0
        var current = forest
        for (i in 0 until n) {
            println("n: $i")
            current = current.next()
            all = current.all()
            trees = all.filterIsInstance(Tree.javaClass).count()
            lumberjacks = all.filterIsInstance(Lumberjack.javaClass).count()
            println("trees: $trees")
            println("lumberjacks: $lumberjacks")
        }
        println(forest.print())


        return trees * lumberjacks
    }

    fun visualiseCycle(lines: List<String>) {
        var forest = parse(lines)
        var all: List<ForestElement>
        var trees: Int = 0
        var lumberjacks: Int = 0
        val n = 700L
        val states = mutableMapOf<Forest, Long>()
        var cycle: Long?
        for (i in 0 until n) {
            cycle = states[forest]
            if (cycle != null) {
                println("cycle found between $cycle and $i")
                val diff = i - cycle
                val rest = n - i
                println("cycle")
                for (k in 0..diff) {
                    println(forest.print())
                    forest = forest.next()
                }
                return
            } else {
                states[forest] = i
            }
            println("n: $i")
            forest = forest.next()
        }
    }
}