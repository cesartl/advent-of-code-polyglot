package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.Graph
import com.ctl.aoc.kotlin.utils.IntSet
import com.ctl.aoc.kotlin.utils.IntSetMapper
import java.util.*

object Day12 {

    private val smallRegex = "[a-z]+".toRegex()

    data class Cave(val id: String) {
        val isSmall: Boolean by lazy { id.matches(smallRegex) }
    }

    data class Path(val last: Cave, val allowSmallTwice: Boolean, val visitedSmallCaves: IntSet, val intSetMapper: IntSetMapper<Cave>) {
        fun canVisit(cave: Cave): Boolean {
            return !cave.isSmall || !intSetMapper.run { visitedSmallCaves.contains(cave) }
        }

        fun canVisit2(cave: Cave): Boolean {
            if (cave.id == "start") {
                return false
            }
            if (cave.id == "end") {
                return !intSetMapper.run { visitedSmallCaves.contains(cave) }
            }
            return allowSmallTwice || (!cave.isSmall || !intSetMapper.run { visitedSmallCaves.contains(cave) })
        }

        fun with(cave: Cave): Path {
            val allowSmallTwice = this.allowSmallTwice && (!cave.isSmall || !intSetMapper.run { visitedSmallCaves.contains(cave) })
            return Path(cave, allowSmallTwice, if (cave.isSmall) (intSetMapper.run { visitedSmallCaves.add(cave) }) else visitedSmallCaves, intSetMapper)
        }


        companion object {
            fun start(allCaves: Collection<Cave>): Path {
                val intSetMapper = IntSetMapper<Cave>()
                allCaves.filter { it.isSmall }.forEach { intSetMapper.add(it) }
                val start = Cave("start")
                return Path(start, true, intSetMapper.run { IntSet().add(start) }, intSetMapper)
            }
        }
    }

    data class CaveGraph(val graph: Graph<Cave>) {

        fun allPaths(canVisit: (Path, Cave) -> Boolean): Sequence<Path> {
            val queue = ArrayDeque<Path>()
            queue.push(Path.start(graph.adjacencyMap.keys))
            return sequence {
                while (queue.isNotEmpty()) {
                    val current = queue.pop()
                    if (current.last.id == "end") {
                        yield(current)
                    } else {
                        graph.outgoingNodes(current.last)
                                .filter { canVisit(current, it) }
                                .forEach { next ->
                                    queue.add(current.with(next))
                                }
                    }
                }
            }
//            fun go(currentPath: Path): Sequence<Path> = sequence {
//                val last = currentPath.last
//                if (last.id == "end") {
//                    yield(currentPath)
//                } else {
//                    graph.outgoingNodes(last)
//                            .filter { canVisit(currentPath, it) }
//                            .forEach { next ->
//                                yieldAll(go(currentPath.with(next)))
//                            }
//                }
//            }
//            return go(Path.start(graph.adjacencyMap.keys))
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