package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.*

object Day12 {

    data class PlantRegion(
        val plants: List<Position>
    )

    fun solve1(input: Sequence<String>): Long {
        val (grid: Grid<Char>, regions) = buildRegions(input)
        return regions.sumOf { priceRegion(it, grid) }
    }

    private fun buildRegions(input: Sequence<String>): Pair<Grid<Char>, MutableList<PlantRegion>> {
        val grid: Grid<Char> = parseGrid(input)
        val regions = mutableListOf<PlantRegion>()
        val visited = mutableSetOf<Position>()
        grid.yRange.forEach { y ->
            grid.xRange.forEach { x ->
                val start = Position(x, y)
                if (!visited.contains(start)) {
                    val region = discoverRegion(start, grid)
                    regions.add(region)
                    region.plants.forEach { visited.add(it) }
                }
            }
        }
        return Pair(grid, regions)
    }

    private fun discoverRegion(start: Position, grid: Grid<Char>): PlantRegion {
        val plants = traversal(
            startNode = start,
            storage = Stack(),
            nodeGenerator = { p ->
                p.adjacent().filter { grid.map[it] == grid.map[p] }
            }
        ).toList()
        return PlantRegion(plants)
    }

    private fun priceRegion(region: PlantRegion, grid: Grid<Char>): Long {
        val perimeter = countPerimeter(region, grid)
        return region.plants.size * perimeter.toLong()
    }

    private fun countPerimeter(region: PlantRegion, grid: Grid<Char>): Int {
        val plantType = grid.map[region.plants.first()] ?: error("No plant type")
        return region.plants.sumOf {
            it.adjacent().count { p -> grid.map[p] != plantType }
        }
    }

    private fun countSides(region: PlantRegion, grid: Grid<Char>): Int {
        val plantType = grid.map[region.plants.first()] ?: error("No plant type")

        return region.plants.sumOf {p ->
            val directions = listOf(N, E, W, S)
            directions.count {d ->
                //check triangle around p
                val t1 = grid.map[d.move(p)]
                val t2 = grid.map[d.rotate(90).move(d.move(p))]
                val t3 = grid.map[d.rotate(90).move(p)]
                (t1 != plantType && t3 != plantType) || (t1 == plantType && t2 != plantType && t3 == plantType)
            }
        }
    }

    private fun priceRegion2(region: PlantRegion, grid: Grid<Char>): Long {
        val plant = grid.map[region.plants.first()]!!
        val area = region.plants.size
        val sides = countSides(region, grid)
        return area * sides.toLong()
    }

    fun solve2(input: Sequence<String>): Long {
        val (grid: Grid<Char>, regions) = buildRegions(input)
        return regions.sumOf { priceRegion2(it, grid) }
    }
}
