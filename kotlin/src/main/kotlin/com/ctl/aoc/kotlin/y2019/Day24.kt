package com.ctl.aoc.kotlin.y2019

object Day24 {
    val size = 5

    fun solve1(lines: Sequence<String>): Long {
        var state = State.parse(lines)
        val visited = mutableSetOf<State>()
        while (!visited.contains(state)) {
            visited.add(state)
            state = state.next()
            state.print()
        }
        println("repeated after ${visited.size}")
        return state.rating()
    }

    data class Point(val x: Int, val y: Int) {
        fun adjacents(): Sequence<Point> = sequence {
            yield(copy(x = x - 1))
            yield(copy(x = x + 1))
            yield(copy(y = y - 1))
            yield(copy(y = y + 1))
        }

        fun toIndex(): Int {
            return y * size + x
        }

        companion object {
            fun toPoint(idx: Int): Point {
                return Point(x = idx % size, y = idx / size)
            }
        }
    }

    data class State(val tiles: BooleanArray) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as State

            if (!tiles.contentEquals(other.tiles)) return false

            return true
        }

        override fun hashCode(): Int {
            return tiles.contentHashCode()
        }

        fun print() {
            (0 until size).forEach { y ->
                (0 until size).forEach { x ->
                    if (tiles[Point(x, y).toIndex()]) {
                        print('#')
                    } else {
                        print('.')
                    }
                }
                println()
            }
            println("-----")
        }

        fun countAdjacent(idx: Int): Int {
            return Point.toPoint(idx).adjacents()
                    .filter { (x, y) -> x in 0 until size && y in 0 until size }
                    .map { it.toIndex() }
                    .count { tiles[it] }
        }

        fun next(): State {
            val newTiles = tiles.copyOf()
            tiles.forEachIndexed { index, b ->
                val count = countAdjacent(index)
//                println("${Point.toPoint(index)} ($index): $count")
                if (b) {
                    if (count != 1) {
                        newTiles[index] = false
                    }
                } else {
                    if (count == 1 || count == 2) {
                        newTiles[index] = true
                    }
                }
            }
            return State(newTiles)
        }

        fun rating(): Long {
            var pow = 1L
            var score = 0L
            tiles.forEach { b ->
                if (b) {
                    score += pow
                }
                pow *= 2
            }
            return score
        }

        companion object {
            fun parse(lines: Sequence<String>): State {
                val tiles = BooleanArray(size * size)
                lines.forEachIndexed { y, line ->
                    line.forEachIndexed { x, c ->
                        if (c == '#') {
                            tiles[Point(x, y).toIndex()] = true
                        }
                    }
                }
                return State(tiles)
            }
        }
    }

}