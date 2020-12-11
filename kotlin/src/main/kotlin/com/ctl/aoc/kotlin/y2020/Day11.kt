package com.ctl.aoc.kotlin.y2020

object Day11 {

    sealed class Cell {
        object Empty : Cell()
        object Occupied : Cell()
        object Floor : Cell()

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
    }

    data class Grid(val map: Map<Position, Cell>, val max: Position = Position(map.keys.map { it.x }.max()
            ?: 0, map.keys.map { it.y }.max() ?: 0)) {
        private fun adjacents(p: Position): Sequence<Position> = p.adjacents().filter { (x, y) ->
            x in 0..max.x && y in 0..max.y
        }

        private fun countOccupied(p: Position): Int {
            return adjacents(p).map { map[it] ?: Cell.Floor }.count { it is Cell.Occupied }
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


        fun simulate(): Grid {
            var previous = this
            var current = this.next()
            while (current != previous) {
                previous = current
                current = current.next()
            }
            return current
        }
    }


    fun pareGrid(input: Sequence<String>): Grid {
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
        val g = pareGrid(input)
        return g.simulate().countOccupied()
    }

    fun solve2(input: Sequence<String>): Int {
        TODO()
    }
}