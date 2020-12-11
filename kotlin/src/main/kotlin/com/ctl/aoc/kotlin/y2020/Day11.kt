package com.ctl.aoc.kotlin.y2020

object Day11 {

    sealed class Cell {
        object Empty : Cell()
        object Occupied : Cell()
        object Floor : Cell()

        fun print(): String {
            return when (this) {
                Empty -> "L"
                Occupied -> "#"
                Floor -> "."
            }
        }

        companion object {
            fun parse(char: Char): Cell? {
                return when (char) {
                    'L' -> Empty
                    '#' -> Occupied
                    '.' -> null
                    else -> throw Error("$char")
                }
            }
        }
    }

    data class Position(val x: Int, val y: Int) {
        fun adjacents() = sequence<Position> {
            yield(Position(x - 1, y - 1))
            yield(Position(x - 1, y))
            yield(Position(x - 1, y + 1))
            yield(Position(x, y - 1))
            yield(Position(x, y + 1))
            yield(Position(x + 1, y - 1))
            yield(Position(x + 1, y))
            yield(Position(x + 1, y + 1))
        }

        fun up() = generateSequence(this) { (x, y) -> Position(x, y - 1) }.drop(1)
        fun upRight() = generateSequence(this) { (x, y) -> Position(x + 1, y - 1) }.drop(1)
        fun right() = generateSequence(this) { (x, y) -> Position(x + 1, y) }.drop(1)
        fun downRight() = generateSequence(this) { (x, y) -> Position(x + 1, y + 1) }.drop(1)
        fun down() = generateSequence(this) { (x, y) -> Position(x, y + 1) }.drop(1)
        fun downLeft() = generateSequence(this) { (x, y) -> Position(x - 1, y + 1) }.drop(1)
        fun left() = generateSequence(this) { (x, y) -> Position(x - 1, y) }.drop(1)
        fun upLeft() = generateSequence(this) { (x, y) -> Position(x - 1, y - 1) }.drop(1)
    }

    data class Grid(val map: Map<Position, Cell>, val max: Position = Position(map.keys.map { it.x }.max()
            ?: 0, map.keys.map { it.y }.max() ?: 0)) {
        private fun adjacents(p: Position): Sequence<Position> = p.adjacents().filter { (x, y) ->
            allowed(x, y)
        }

        private fun up(p: Position) = p.up().takeWhile { (x, y) -> allowed(x, y) }
        private fun upRight(p: Position) = p.upRight().takeWhile { (x, y) -> allowed(x, y) }
        private fun right(p: Position) = p.right().takeWhile { (x, y) -> allowed(x, y) }
        private fun downRight(p: Position) = p.downRight().takeWhile { (x, y) -> allowed(x, y) }
        private fun down(p: Position) = p.down().takeWhile { (x, y) -> allowed(x, y) }
        private fun downLeft(p: Position) = p.downLeft().takeWhile { (x, y) -> allowed(x, y) }
        private fun left(p: Position) = p.left().takeWhile { (x, y) -> allowed(x, y) }
        private fun upLeft(p: Position) = p.upLeft().takeWhile { (x, y) -> allowed(x, y) }

        private fun allowed(x: Int, y: Int) = x in 0..max.x && y in 0..max.y

        private fun countOccupied(p: Position): Int {
            return adjacents(p).map { map[it] ?: Cell.Floor }.count { it is Cell.Occupied }
        }

        private fun occupiedInSight(positions: Sequence<Position>): Int {
            return when (positions.map { map[it] ?: Cell.Floor }.dropWhile { it is Cell.Floor }.firstOrNull()) {
                Cell.Occupied -> 1
                else -> 0
            }
        }

        fun occupiedInSight(p: Position): Int {
            return listOf(up(p), upRight(p), right(p), downRight(p), down(p), downLeft(p), left(p), upLeft(p))
                    .map { occupiedInSight(it) }.sum()
        }

        fun countOccupied(): Int {
            return map.values.count { it is Cell.Occupied }
        }

        fun next(): Grid {
            val newMap = map.entries.fold(mutableMapOf<Position, Cell>()) { newMap, (position, cell) ->
                val (p, c) = when (cell) {
                    Cell.Empty -> if (countOccupied(position) == 0) position to Cell.Occupied else position to cell
                    Cell.Occupied -> if (countOccupied(position) >= 4) position to Cell.Empty else position to cell
                    Cell.Floor -> position to cell
                }
                newMap[p] = c
                newMap
            }
            return Grid(newMap)
        }

        fun next2(): Grid {
            val newMap = map.entries.fold(mutableMapOf<Position, Cell>()) { newMap, (position, cell) ->
                val (p, c) = when (cell) {
                    Cell.Empty -> if (occupiedInSight(position) == 0) position to Cell.Occupied else position to cell
                    Cell.Occupied -> if (occupiedInSight(position) >= 5) position to Cell.Empty else position to cell
                    Cell.Floor -> position to cell
                }
                newMap[p] = c
                newMap
            }
            return Grid(newMap)
        }

        fun print() {
            (0..max.y).forEach { y ->
                (0..max.x).forEach { x ->
                    print((map[Position(x, y)] ?: Cell.Floor).print())
                }
                println()
            }
            println()
        }


        fun simulate(): Grid {
            var previous: Grid? = null
            var current = this
            while (current != previous) {
                previous = current
                current = current.next()
            }
            return current
        }

        fun simulate2(): Grid {
            var previous: Grid? = null
            var current = this
            while (current != previous) {
                previous = current
                current = current.next2()
            }
            return current
        }
    }


    fun parseGrid(input: Sequence<String>): Grid {
        val map = input.mapIndexed { y, row ->
            row.mapIndexed { x, char ->
                val p = Position(x, y)
                val cell = Cell.parse(char)
                cell?.let { (p to it) }
            }.filterNotNull()
        }.flatten().toMap()
        return Grid(map)
    }

    fun solve1(input: Sequence<String>): Int {
        val g = parseGrid(input)
        return g.simulate().countOccupied()
    }

    fun solve2(input: Sequence<String>): Int {
        val g = parseGrid(input)
        return g.simulate2().countOccupied()
    }
}