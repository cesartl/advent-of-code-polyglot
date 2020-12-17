package com.ctl.aoc.kotlin.y2020

object Day17 {

    fun solve1(input: Sequence<String>): Int {
        val grid = Grid.parse(input)
        val after = grid.next(6)
        return after.cells.size
    }

    fun solve2(input: Sequence<String>): Int {
        TODO()
    }

    data class Position(val x: Int, val y: Int, val z: Int) {
        fun neighbours(): Sequence<Position> = sequence {
            (-1..1).forEach { dx ->
                (-1..1).forEach { dy ->
                    (-1..1).forEach { dz ->
                        val p = Position(x + dx, y + dy, z + dz)
                        if (p != this@Position) {
                            yield(p)
                        }
                    }
                }
            }
        }
    }

    data class Grid(val cells: Set<Position>) {
        val xRange = range { it.x }
        val yRange = range { it.y }
        val zRange = range { it.z }

        private fun range(f: (Position) -> Int): IntRange {
            val coordinates = cells.map { f(it) }
            return (coordinates.min()!! - 1)..(coordinates.max()!! + 1)
        }

        private fun countActiveNeighbours(p: Position): Int {
            return p.neighbours().filter { cells.contains(it) }.count()
        }

        fun next(): Grid {
//            this.print()
            val newCells = mutableSetOf<Position>()
            xRange.forEach { x ->
                yRange.forEach { y ->
                    zRange.forEach { z ->
                        val p = Position(x, y, z)
                        val activeNeighbours = countActiveNeighbours(p)
                        if (cells.contains(p)) {
                            if ((2..3).contains(activeNeighbours)) {
                                newCells.add(p)
                            }
                        } else {
                            if (activeNeighbours == 3) {
                                newCells.add(p)
                            }
                        }
                    }
                }
            }
            return Grid(newCells)
        }

        fun print() {
            println("----")
            zRange.forEach { z ->
                println("z=$z")
                yRange.forEach { y ->
                    println(xRange.map { Position(it, y, z) }.map { if (cells.contains(it)) "#" else "." }.joinToString(""))
                }
            }
            println("----")
        }

        companion object {
            fun parse(lines: Sequence<String>): Grid {
                val z = 0
                val cells = mutableSetOf<Position>()
                lines.forEachIndexed { y, line ->
                    line.forEachIndexed { x, c ->
                        if (c == '#') {
                            cells.add(Position(x, y, z))
                        }
                    }
                }
                return Grid(cells)
            }
        }
    }

    tailrec fun Grid.next(n: Int): Grid {
//        println("n: $n")
        return if (n == 0) {
            this
        } else {
            this.next().next(n - 1)
        }
    }
}