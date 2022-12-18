package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.Position3d
import com.ctl.aoc.kotlin.utils.Queue
import com.ctl.aoc.kotlin.utils.toPosition3d
import com.ctl.aoc.kotlin.utils.traversal

object Day18 {

    fun solve1(input: Sequence<String>): Int {
        val cubes = input.map { it.toPosition3d() }.toList()
        val index = cubes.toSet()
        return cubes.sumOf { cube ->
            6 - countNeighbours(cube, index)
        }
    }

    private fun countNeighbours(cube: Position3d, index: Set<Position3d>): Int {
        return cube.adjacent().count { index.contains(it) }
    }

    /**
     * Surrounding positions, no diagonal
     */
    private fun Position3d.adjacent(): Sequence<Position3d> = sequenceOf(
        Position3d(1, 0, 0),
        Position3d(-1, 0, 0),
        Position3d(0, 1, 0),
        Position3d(0, -1, 0),
        Position3d(0, 0, 1),
        Position3d(0, 0, -1),
    ).map { this + it }

    /**
     * We do a BFS of all the air cells around the lava droplets. Once we have all the
     * air cells, we count how many droplets they are touching
     */
    fun solve2(input: Sequence<String>): Int {
        val cubes = input.map { it.toPosition3d() }.toList()

        val dropletSet = cubes.toSet()

        /**
         * We find the range +1 around all the droplets
         */
        val xs = cubes.map { it.x }
        val ys = cubes.map { it.y }
        val zs = cubes.map { it.z }
        val xRange = (xs.min() - 1..xs.max() + 1)
        val yRange = (ys.min() - 1..ys.max() + 1)
        val zRange = (zs.min() - 1..zs.max() + 1)

        //we pick a random start for the air cells
        val start = Position3d(xRange.first, yRange.first, zRange.first)

        //we do a bfs to find all the air cells
        val airCells = traversal(
            start,
            Queue(),
            { it.toString() },
            {
                it.adjacent()
                    // we ignore the air cells outside of the +1 boundary
                    .filter { p -> p.inRange(xRange, yRange, zRange) }
                    //air cells blocked by laval droplets
                    .filterNot { p -> dropletSet.contains(p) }
            }
        )

        //once we have all the air cells, we count how many droplets they are touching
        return airCells
            .map { airCell ->
                airCell.adjacent().count { dropletSet.contains(it) }
            }.sum()
    }

    private fun Position3d.inRange(xRange: IntRange, yRange: IntRange, zRange: IntRange): Boolean {
        return x in xRange && y in yRange && z in zRange
    }

}
