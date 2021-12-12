package com.ctl.aoc.kotlin.y2019

object Day24 {
    val size = 5

    fun solve2(lines: Sequence<String>): Long {
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

    fun solve2(lines: Sequence<String>, n: Int): Int {
        var state = State.parse(lines)
        val recursiveState = RecursiveState(mapOf(0 to state.tiles))
        val end = next(n, recursiveState)
//        end.levels.toList().sortedBy { it.first }.forEach { (level, tiles) ->
//            println("Level $level")
//            State(tiles).print()
//        }
        return end.countBugs()
    }

    data class Point(val x: Int, val y: Int) {
        fun adjacent(): Sequence<Point> = sequence {
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

    tailrec fun next(n: Int, recursiveState: RecursiveState): RecursiveState {
        return if (n == 0) {
            recursiveState
        } else {
            next(n - 1, recursiveState.next())
        }
    }

    data class RecursiveState(val levels: Map<Int, BooleanArray>) {

        fun countBugs(): Int {
            return levels.values.map { tiles -> tiles.count { it } }.sum()
        }

        fun next(): RecursiveState {
            val nextLevels = mutableMapOf<Int, BooleanArray>()
            val minLevel = levels.keys.minOrNull()!!
            val maxLevel = levels.keys.maxOrNull()!!
            levels.forEach { (level, tiles) ->
                nextLevels[level] = tiles.copyOf()
            }
            nextLevels[minLevel - 1] = BooleanArray(size * size)
            nextLevels[maxLevel + 1] = BooleanArray(size * size)

            nextLevels.forEach { (level, newTiles) ->
                newTiles.forEachIndexed { index, _ ->
                    if (index != 12) {
                        val b = levels[level]?.let { it[index] } ?: false
                        val count = countAdjacentBugs(level, Point.toPoint(index))
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
                }
            }
            nextLevels[minLevel - 1]?.let {
                if (isEmpty(it)) {
//                    println("Removing -1")
                    nextLevels.remove(minLevel - 1)
                }
            }
            nextLevels[minLevel + 1]?.let {
                if (isEmpty(it)) {
//                    println("Removing +1")
                    nextLevels.remove(minLevel + 1)
                }
            }
            return RecursiveState(nextLevels)
        }

        private fun isEmpty(booleanArray: BooleanArray): Boolean = booleanArray.all { !it }

        private fun countAdjacentBugs(level: Int, point: Point): Int {
            return point.adjacent(level).count { (n, p) ->
                levels[n]?.let { tiles -> tiles[p.toIndex()] } ?: false
            }
        }

        private fun Point.adjacent(level: Int): Sequence<Pair<Int, Point>> = sequence {
            // x + 1
            if (x == 1 && y == 2) {
                (0 until size).forEach { y ->
                    yield(level + 1 to Point(0, y))
                }
            } else if (x == 4) {
                yield(level - 1 to Point(x = 3, y = 2))
            } else {
                yield(level to copy(x = x + 1))
            }

            // x - 1
            if (x == 3 && y == 2) {
                (0 until size).forEach { y ->
                    yield(level + 1 to Point(size - 1, y))
                }
            } else if (x == 0) {
                yield(level - 1 to Point(x = 1, y = 2))
            } else {
                yield(level to copy(x = x - 1))
            }

            //y + 1
            if (x == 2 && y == 1) {
                (0 until size).forEach { x ->
                    yield(level + 1 to Point(x, 0))
                }
            } else if (y == 4) {
                yield(level - 1 to Point(x = 2, y = 3))
            } else {
                yield(level to copy(y = y + 1))
            }

            //y - 1
            if (x == 2 && y == 3) {
                (0 until size).forEach { x ->
                    yield(level + 1 to Point(x, size - 1))
                }
            } else if (y == 0) {
                yield(level - 1 to Point(x = 2, y = 1))
            } else {
                yield(level to copy(y = y - 1))
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
            return Point.toPoint(idx).adjacent()
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