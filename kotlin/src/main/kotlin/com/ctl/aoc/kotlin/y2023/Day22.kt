package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.Graph
import com.ctl.aoc.kotlin.utils.Position3d
import com.ctl.aoc.kotlin.utils.takeWhileInclusive
import com.ctl.aoc.kotlin.utils.toPosition3d
import kotlin.math.min


sealed class SandLine {

    abstract val from: Position3d
    abstract val to: Position3d

    abstract val increment: Position3d

    fun move(offset: Position3d): SandLine = when (this) {
        is XLine -> this.copy(from = from + offset, to = to + offset)
        is YLine -> this.copy(from = from + offset, to = to + offset)
        is ZLine -> this.copy(from = from + offset, to = to + offset)
        is SingleCube -> this.copy(from = from + offset)
    }

    open fun points(): Sequence<Position3d> {
        return generateSequence(from) { it + increment }.takeWhileInclusive { it != to }
    }

    data class XLine(override val from: Position3d, override val to: Position3d) : SandLine() {
        override val increment: Position3d
            get() = Position3d(1, 0, 0)
    }

    data class YLine(override val from: Position3d, override val to: Position3d) : SandLine() {
        override val increment: Position3d
            get() = Position3d(0, 1, 0)
    }

    data class ZLine(override val from: Position3d, override val to: Position3d) : SandLine() {
        override val increment: Position3d
            get() = Position3d(0, 0, 1)
    }

    data class SingleCube(override val from: Position3d, override val to: Position3d) : SandLine() {
        override val increment: Position3d
            get() = Position3d(0, 0, 0)

        override fun points(): Sequence<Position3d> {
            return sequenceOf(from)
        }
    }
}

fun xLine(a: Position3d, b: Position3d): SandLine.XLine {
    assert(a.x != b.x)
    assert(a.y == b.y)
    assert(a.z == b.z)
    val from: Position3d
    val to: Position3d
    if (a.x < b.x) {
        from = a
        to = b
    } else {
        from = b
        to = a
    }
    return SandLine.XLine(from, to)
}

fun yLine(a: Position3d, b: Position3d): SandLine.YLine {
    assert(a.x == b.x)
    assert(a.y != b.y)
    assert(a.z == b.z)
    val from: Position3d
    val to: Position3d
    if (a.y < b.y) {
        from = a
        to = b
    } else {
        from = b
        to = a
    }
    return SandLine.YLine(from, to)
}

fun zLine(a: Position3d, b: Position3d): SandLine.ZLine {
    assert(a.x == b.x)
    assert(a.y == b.y)
    assert(a.z != b.z)
    val from: Position3d
    val to: Position3d
    if (a.z < b.z) {
        from = a
        to = b
    } else {
        from = b
        to = a
    }
    return SandLine.ZLine(from, to)
}

fun sandLine(a: Position3d, b: Position3d): SandLine {
    return if (a.x != b.x) {
        xLine(a, b)
    } else if (a.y != b.y) {
        yLine(a, b)
    } else if (a.z != b.z) {
        zLine(a, b)
    } else {
        SandLine.SingleCube(a, b)
    }
}

data class SandBrick(
    val id: Int,
    val p1: Position3d,
    val p2: Position3d,
    val line: SandLine
) {
    fun moveDown(): SandBrick {
        val offset = Position3d(0, 0, -1)
        return copy(
            p1 = p1 + offset,
            p2 = p2 + offset,
            line = line.move(offset)
        )
    }

    val points: Set<Position3d> by lazy {
        line.points().toSet()
    }

    val minZ: Int by lazy {
        min(p1.z, p2.z)
    }
}

private fun String.parseBrick(id: Int): SandBrick {
    val (a, b) = this
        .splitToSequence("~")
        .map { it.toPosition3d() }
        .toList()
    val line = sandLine(a, b)
    return SandBrick(id, a, b, line)
}

fun settleBricks(input: Sequence<String>): List<SandBrick> {
    val bricks = input.mapIndexed { i, s -> s.parseBrick(i) }.toList()
    val sortedBricks = bricks.sortedBy { it.minZ }
    val restedPoints = mutableSetOf<Position3d>()
    val settledBrick = mutableListOf<SandBrick>()
    sortedBricks.forEach { brick ->
        if (brick.minZ == 1) {
            restedPoints.addAll(brick.points)
            settledBrick.add(brick)
        } else {
            var current: SandBrick
            var next: SandBrick = brick
            do {
                current = next
                next = current.moveDown()
            } while (next.points.none { restedPoints.contains(it) } && next.minZ > 0)
            restedPoints.addAll(current.points)
            settledBrick.add(current)
        }
    }
    return settledBrick
}

fun fallingBricks(start: SandBrick, originalGraph: Graph<SandBrick>): Int {
    val queue = ArrayDeque<SandBrick>()
    val graph = originalGraph.copy()
    queue.addFirst(start)
    var count = 0
    var current: SandBrick
    while (queue.isNotEmpty()) {
        current = queue.removeFirst()
        count++
        val above = graph.incomingNodes(current)
        above.forEach {
            graph.removeEdge(it, current)
            if (graph.outgoingNodes(it).isEmpty()) {
                queue.addLast(it)
            }
        }
    }
    return count - 1
}

private fun canBeRemoved(brick: SandBrick, graph: Graph<SandBrick>): Boolean {
    return graph.incomingNodes(brick).all { graph.outgoingNodes(it).size > 1 }
}

private fun buildGraph(restedBrick: List<SandBrick>): Graph<SandBrick> {
    val pointsToBricks = restedBrick
        .asSequence()
        .flatMap { brick -> brick.points.map { it to brick } }
        .toMap()

    val graph = Graph<SandBrick>()
    restedBrick.forEach { brick ->
        brick.points
            .asSequence()
            .filter { it.z == brick.minZ }
            .map { it - Position3d(0, 0, 1) }
            .mapNotNull { pointsToBricks[it] }
            .forEach {
                graph.addDirectedEdge(brick, it)
            }
    }
    return graph
}

object Day22 {
    fun solve1(input: Sequence<String>): Int {
        val bricks = settleBricks(input)
        val graph = buildGraph(bricks)
        //        graph.describe { ('a'+it.id).toString() }
        return bricks.count { canBeRemoved(it, graph) }
    }

    fun solve2(input: Sequence<String>): Int {
        val bricks = settleBricks(input)
        val graph = buildGraph(bricks)
        val falling = bricks.map { fallingBricks(it, graph) }
        return falling.sum()
    }


}
