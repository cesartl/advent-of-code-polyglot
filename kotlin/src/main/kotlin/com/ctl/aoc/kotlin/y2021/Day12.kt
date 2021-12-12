package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.Graph

object Day12 {

    private val smallRegex = "[a-z]+".toRegex()

    @JvmInline
    value class Cave(val id: String) {
        val isSmall: Boolean
            get() = id.matches(smallRegex)
    }

    data class Path(val last: Cave, val allowSmallTwice: Boolean, val visitedSmallCaves: Set<Cave>) {
        fun canVisit(cave: Cave): Boolean {
            return !(cave.isSmall && visitedSmallCaves.contains(cave))
        }

        fun canVisit2(cave: Cave): Boolean {
            if (cave.id == "start") {
                return false
            }
            if (cave.id == "end") {
                return !visitedSmallCaves.contains(cave)
            }
            return allowSmallTwice || !(cave.isSmall && visitedSmallCaves.contains(cave))
        }

        fun with(cave: Cave): Path {
            val allowSmallTwice = this.allowSmallTwice && (!cave.isSmall || !visitedSmallCaves.contains(cave))
            return Path(cave, allowSmallTwice, if (cave.isSmall) (visitedSmallCaves + cave) else visitedSmallCaves)
        }


        companion object {
            fun start(): Path = Path(Cave("start"), true, setOf(Cave("start")))
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
        return caveGraph.allPaths { path, cave -> path.canVisit(cave) }.count()
    }

    fun solve2(input: Sequence<String>): Int {
        val caveGraph = CaveGraph.parse(input)
        return caveGraph.allPaths { path, cave -> path.canVisit2(cave) }.count()
    }
}