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

    sealed class Direction {
        object N : Direction()
        object NE : Direction()
        object E : Direction()
        object SE : Direction()
        object S : Direction()
        object SW : Direction()
        object W : Direction()
        object NW : Direction()

        fun move(p: Position): Position {
            val (x, y) = p
            return when (this) {
                N -> Position(x, y - 1)
                NE -> Position(x + 1, y - 1)
                E -> Position(x + 1, y)
                SE -> Position(x + 1, y + 1)
                S -> Position(x, y + 1)
                SW -> Position(x - 1, y + 1)
                W -> Position(x - 1, y)
                NW -> Position(x - 1, y - 1)
            }
        }
    }

    val allDirections = sequenceOf(Direction.N, Direction.NE, Direction.E, Direction.SE, Direction.S, Direction.SW, Direction.W, Direction.NW)

    data class Position(val x: Int, val y: Int) {
        fun adjacents(): Sequence<Position> = allDirections.map { it.move(this) }
        fun lineOfSights(): Sequence<Sequence<Position>> = allDirections.map { direction -> generateSequence(this, direction::move).drop(1) }
    }

    data class Grid(val map: Map<Position, Cell>, val max: Position = Position(map.keys.map { it.x }.max()
            ?: 0, map.keys.map { it.y }.max() ?: 0)) {

        private fun adjacents(p: Position): Sequence<Position> = p.adjacents().filter { isPositionValid(it) }
        private fun lineOfSights(p: Position): Sequence<Sequence<Position>> = p.lineOfSights().map { line -> line.takeWhile { isPositionValid(it) } }

        private fun isPositionValid(p: Position): Boolean = p.x in 0..max.x && p.y in 0..max.y

        private fun countOccupied(p: Position): Int {
            return adjacents(p).map { map[it] ?: Cell.Floor }.count { it is Cell.Occupied }
        }

        private fun occupiedInSight(p: Position): Int {
            return lineOfSights(p).count { firstInSight(it) is Cell.Occupied }
        }

        private fun firstInSight(positions: Sequence<Position>): Cell? {
            return positions.map { map[it] ?: Cell.Floor }.dropWhile { it is Cell.Floor }.firstOrNull()
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
            return this.copy(map = newMap)
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
            return this.copy(map = newMap)
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


        fun simulate(next: (Grid) -> Grid): Grid {
            var previous: Grid? = null
            var current = this
            while (current != previous) {
                previous = current
                current = next(current)
            }
            return current
        }
    }

    fun solve1(input: Sequence<String>): Int {
        val g = parseGrid(input)
        return g.simulate(Grid::next).countOccupied()
    }

    fun solve2(input: Sequence<String>): Int {
        val g = parseGrid(input)
        return g.simulate(Grid::next2).countOccupied()
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
}