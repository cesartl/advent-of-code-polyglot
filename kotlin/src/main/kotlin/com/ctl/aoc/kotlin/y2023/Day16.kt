package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.*
import kotlin.collections.set

object Day16 {

    sealed interface Element {

        fun print(): String = when (this) {
            BackwardMirror -> "\\"
            ForwardMirror -> "/"
            HSplitter -> "-"
            VSplitter -> "-"
        }

        data object VSplitter : Element {
            override fun next(orientation: Orientation): Sequence<Orientation> {
                return if (orientation == E || orientation == W) {
                    sequenceOf(N, S)
                } else {
                    sequenceOf(orientation)
                }
            }
        }

        data object HSplitter : Element {
            override fun next(orientation: Orientation): Sequence<Orientation> {
                return if (orientation == N || orientation == S) {
                    sequenceOf(E, W)
                } else {
                    sequenceOf(orientation)
                }
            }
        }

        data object ForwardMirror : Element {
            override fun next(orientation: Orientation): Sequence<Orientation> {
                val o = when (orientation) {
                    E -> N
                    N -> E
                    S -> W
                    W -> S
                }
                return sequenceOf(o)
            }
        }

        data object BackwardMirror : Element {
            override fun next(orientation: Orientation): Sequence<Orientation> {
                val o = when (orientation) {
                    E -> S
                    N -> W
                    S -> E
                    W -> N
                }
                return sequenceOf(o)
            }
        }

        fun next(orientation: Orientation): Sequence<Orientation>
    }

    data class Contraption(
        val elements: Map<Position, Element>,
        val xRange: IntRange,
        val yRange: IntRange,
    ) {
        fun inScope(p: Position): Boolean {
            val (x, y) = p
            return x in xRange && y in yRange
        }

        fun allStarts(): Sequence<LightBeam> = sequence {
            //top row y=0 S
            xRange.forEach { x ->
                yield(LightBeam(Position(x, 0), S))
            }

            //bottom row y=max N
            xRange.forEach { x ->
                yield(LightBeam(Position(x, yRange.last), N))
            }

            //left column x=0 E
            yRange.forEach { y ->
                yield(LightBeam(Position(0, y), E))
            }

            //right column x=max W
            yRange.forEach { y ->
                yield(LightBeam(Position(xRange.last, y), W))
            }
        }
    }

    private fun parseContraction(input: Sequence<String>): Contraption {
        val elements = mutableMapOf<Position, Element>()
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                val p = Position(x, y)
                val element = when (c) {
                    '|' -> Element.VSplitter
                    '-' -> Element.HSplitter
                    '/' -> Element.ForwardMirror
                    '\\' -> Element.BackwardMirror
                    else -> null
                }
                element?.let {
                    elements[p] = it
                }
            }
        }
        val yRange = 0..<input.count()
        val xRange = 0..<input.first().length
        return Contraption(elements, xRange, yRange)
    }

    data class LightBeam(
        val position: Position,
        val orientation: Orientation
    )

    class ContraptionState(
        private val contraption: Contraption
    ) {
        val illuminated: MutableSet<Position> = mutableSetOf()

        fun print() {
            contraption.yRange.forEach { y ->
                contraption.xRange.forEach { x ->
                    val p = Position(x, y)
                    val c = if (illuminated.contains(p)) {
                        '#'
                    } else {
                        val e = contraption.elements[p]
                        e?.print() ?: '.'
                    }
                    print(c)
                }
                println()
            }
            println()
        }

        fun illuminate(start: LightBeam) {
            val queue = ArrayDeque<LightBeam>()
            val visited = mutableSetOf<LightBeam>()
            queue.add(start)
            while (!queue.isEmpty()) {
                val next = queue.removeFirst()
                val (position, orientation) = next
                if (!contraption.inScope(position) || visited.contains(next)) {
                    continue
                }
                visited.add(next)
                illuminated.add(position)
                val element = contraption.elements[position]
                if (element == null) {
                    queue.addLast(LightBeam(orientation.move(position), orientation))
                } else {
                    element.next(orientation).forEach { nextOrientation ->
                        queue.addLast(LightBeam(nextOrientation.move(position), nextOrientation))
                    }
                }
            }
        }
    }

    fun solve1(input: Sequence<String>): Int {
        val contraption = parseContraction(input)
        val state = ContraptionState(contraption)
        state.illuminate(LightBeam(Position(0, 0), E))
        return state.illuminated.size
    }

    fun solve2(input: Sequence<String>): Int {
        val contraption = parseContraction(input)
        return contraption.allStarts().map { start ->
            val state = ContraptionState(contraption)
            state.illuminate(start)
            state.illuminated.size
        }.max()
    }
}
