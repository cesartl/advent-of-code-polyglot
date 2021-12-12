package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.Graph

object Day12 {

    data class Cave(val id: String) {
        val isSmall: Boolean = id.matches("[a-z]+".toRegex())
    }

    data class Path(val caves: List<Cave>, val allowSmallTwice: Boolean) {
        val last: Cave by lazy { caves[caves.size - 1] }
        fun canVisit(cave: Cave): Boolean {
            return !(cave.isSmall && caves.contains(cave))
        }

        fun canVisit2(cave: Cave): Boolean {
            if (cave.id == "start") {
                return false
            }
            if (cave.id == "end") {
                return !caves.contains(cave)
            }
            return allowSmallTwice || !(cave.isSmall && caves.contains(cave))
        }

        fun with(cave: Cave): Path {
            val allowSmallTwice = this.allowSmallTwice && (!cave.isSmall || !caves.contains(cave))
            return Path(caves + cave, allowSmallTwice)
        }

        fun print(): String = caves.joinToString(separator = "-") { it.id }

        companion object {
            fun start(): Path = Path(listOf(Cave("start")), true)
        }
    }

    data class CaveGraph(val graph: Graph<Cave>) {

        fun allPaths(canVisit: (Path, Cave) -> Boolean): Sequence<Path> {
            fun go(currentPath: Path): Sequence<Path> = sequence {
                val last = currentPath.last
                if (last.id == "end") {
                    yield(currentPath)
                } else {
                    graph.outgoingNodes(last)
                            .filter { canVisit(currentPath, it) }
                            .forEach { next ->
                                yieldAll(go(currentPath.with(next)))
                            }
                }
            }
            return go(Path.start())
        }

        companion object {
            fun parse(lines: Sequence<String>): CaveGraph {
                val graph = Graph<Cave>()
                lines.forEach { line ->
                    val (source, dest) = line.split("-")
                    graph.addEdge(Cave(source), Cave(dest))
                }
                return CaveGraph(graph)
            }
        }
    }

    fun solve1(input: Sequence<String>): Int {
        val caveGraph = CaveGraph.parse(input)
        val paths = caveGraph.allPaths { path, cave -> path.canVisit(cave) }
                .map { it.print() }
                .toList()
        return paths.size
    }

    fun solve2(input: Sequence<String>): Int {
        val caveGraph = CaveGraph.parse(input)
        val paths = caveGraph.allPaths { path, cave -> path.canVisit2(cave) }
                .map { it.print() }
                .toList()
        return paths.size
    }
}