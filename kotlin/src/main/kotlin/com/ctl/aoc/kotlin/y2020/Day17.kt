package com.ctl.aoc.kotlin.y2020

object Day17 {

    fun solve1(input: Sequence<String>): Int {
        val grid = Grid3D.parse(input)
        val after = grid.next(6)
        return after.cells.size
    }

    fun solve2(input: Sequence<String>): Int {
        val grid = Grid4D.parse(input)
        val after = grid.next(6)
        return after.cells.size
    }

    data class Position3D(val x: Int, val y: Int, val z: Int) {
        fun neighbours(): Sequence<Position3D> = sequence {
            (-1..1).forEach { dx ->
                (-1..1).forEach { dy ->
                    (-1..1).forEach { dz ->
                        val p = Position3D(x + dx, y + dy, z + dz)
                        if (p != this@Position3D) {
                            yield(p)
                        }
                    }
                }
            }
        }
    }

    data class Position4D(val x: Int, val y: Int, val z: Int, val w: Int) {
        fun neighbours(): Sequence<Position4D> = sequence {
            (-1..1).forEach { dx ->
                (-1..1).forEach { dy ->
                    (-1..1).forEach { dz ->
                        (-1..1).forEach { dw ->
                            val p = Position4D(x + dx, y + dy, z + dz, w + dw)
                            if (p != this@Position4D) {
                                yield(p)
                            }
                        }
                    }
                }
            }
        }
    }

    data class Grid3D(val cells: Set<Position3D>) {
        val xRange = range { it.x }
        val yRange = range { it.y }
        val zRange = range { it.z }

        private fun range(f: (Position3D) -> Int): IntRange {
            val coordinates = cells.map { f(it) }
            return (coordinates.min()!! - 1)..(coordinates.max()!! + 1)
        }

        private fun countActiveNeighbours(p: Position3D): Int {
            return p.neighbours().filter { cells.contains(it) }.count()
        }

        fun next(): Grid3D {
            val newCells = mutableSetOf<Position3D>()
            xRange.forEach { x ->
                yRange.forEach { y ->
                    zRange.forEach { z ->
                        val p = Position3D(x, y, z)
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
            return Grid3D(newCells)
        }

        fun print() {
            println("----")
            zRange.forEach { z ->
                println("z=$z")
                yRange.forEach { y ->
                    println(xRange.map { Position3D(it, y, z) }.map { if (cells.contains(it)) "#" else "." }.joinToString(""))
                }
            }
            println("----")
        }

        companion object {
            fun parse(lines: Sequence<String>): Grid3D {
                val z = 0
                val cells = mutableSetOf<Position3D>()
                lines.forEachIndexed { y, line ->
                    line.forEachIndexed { x, c ->
                        if (c == '#') {
                            cells.add(Position3D(x, y, z))
                        }
                    }
                }
                return Grid3D(cells)
            }
        }
    }

    data class Grid4D(val cells: Set<Position4D>) {
        val xRange = range { it.x }
        val yRange = range { it.y }
        val zRange = range { it.z }
        val wRange = range { it.w }

        private fun range(f: (Position4D) -> Int): IntRange {
            val coordinates = cells.map { f(it) }
            return (coordinates.min()!! - 1)..(coordinates.max()!! + 1)
        }

        private fun countActiveNeighbours(p: Position4D): Int {
            return p.neighbours().filter { cells.contains(it) }.count()
        }

        fun next(): Grid4D {
            val newCells = mutableSetOf<Position4D>()
            xRange.forEach { x ->
                yRange.forEach { y ->
                    zRange.forEach { z ->
                        wRange.forEach { w ->
                            val p = Position4D(x, y, z, w)
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
            }
            return Grid4D(newCells)
        }

        companion object {
            fun parse(lines: Sequence<String>): Grid4D {
                val z = 0
                val w = 0
                val cells = mutableSetOf<Position4D>()
                lines.forEachIndexed { y, line ->
                    line.forEachIndexed { x, c ->
                        if (c == '#') {
                            cells.add(Position4D(x, y, z, w))
                        }
                    }
                }
                return Grid4D(cells)
            }
        }
    }

    tailrec fun Grid3D.next(n: Int): Grid3D {
        return if (n == 0) {
            this
        } else {
            this.next().next(n - 1)
        }
    }

    tailrec fun Grid4D.next(n: Int): Grid4D {
        return if (n == 0) {
            this
        } else {
            this.next().next(n - 1)
        }
    }
}