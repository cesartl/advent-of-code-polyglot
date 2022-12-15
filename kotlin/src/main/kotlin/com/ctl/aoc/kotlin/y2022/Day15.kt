package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.Position
import kotlin.math.absoluteValue


private val regex = """Sensor at x=([\d-]+), y=([\d-]+): closest beacon is at x=([\d-]+), y=([\d-]+)""".toRegex()

object Day15 {

    data class SensorSpec(val sensor: Position, val beacon: Position) {

        val distance = sensor.distance(beacon)
        fun candidates(): Sequence<Position> = sequence {
            val d = distance + 1
            (-d..d).forEach { dX ->
                yield(sensor + Position(dX, d - dX))
                yield(sensor + Position(dX, d + dX))
            }
        }
    }

    private fun String.parseSpec(): SensorSpec {
        return regex.matchEntire(this)
            ?.let {
                val ints = it.groupValues.drop(1).map { it.toInt() }
                val p = Position(ints[0], ints[1])
                val s = Position(ints[2], ints[3])
                SensorSpec(p, s)
            } ?: error(this)
    }

    sealed class Terrain {
        object Sensor : Terrain()
        object Beacon : Terrain()
        object Empty : Terrain()
        object Free : Terrain()
    }

    data class World(val terrain: Map<Position, Terrain>) {
        fun print() {
            val xRange = (terrain.keys.minOfOrNull { it.x } ?: 0)..(terrain.keys.maxOfOrNull { it.x } ?: 0)
            val yRange = (terrain.keys.minOfOrNull { it.y } ?: 0)..(terrain.keys.maxOfOrNull { it.y } ?: 0)
            yRange.forEach { y ->
                xRange.forEach { x ->
                    val p = Position(x, y)
                    val c = when (terrain[p]) {
                        Terrain.Beacon -> "B"
                        Terrain.Empty -> "#"
                        Terrain.Sensor -> "S"
                        Terrain.Free -> "."
                        null -> "?"
                    }
                    print(c)
                }
                println()
            }
        }
    }

    fun buildWorld(input: Sequence<String>, targetY: Int): World {
        val terrain = mutableMapOf<Position, Terrain>()
        input
            .map { it.parseSpec() }
            .filter { spec ->
                val d = spec.sensor.distance(spec.beacon)
                (targetY - spec.sensor.y).absoluteValue <= d + 1
            }
            .forEach { (sensor, beacon) ->
                terrain[sensor] = Terrain.Sensor
                terrain[beacon] = Terrain.Beacon
                val d = sensor.distance(beacon)
                (-d until d).forEach { y ->
                    if (y + sensor.y == targetY) {
                        (-d until d).forEach { x ->
                            val p = sensor + Position(x, y)
                            if (p.distance(sensor) <= d && !terrain.containsKey(p)) {
                                terrain[p] = Terrain.Empty
                            }
                        }
                    }
                }
            }
        return World(terrain)
    }

    //Attempt at coordinate compression
    private fun findDistressBeacon(input: Sequence<String>, max: Int): Position {
        val specs = input
            .map { it.parseSpec() }
        val terrain = mutableMapOf<Position, Terrain>()
        val xs = specs
            .flatMap { listOf(it.sensor.x, it.beacon.x, it.sensor.x + 1, it.beacon.x + 1) }
            .sorted()
            .toList()
        val ys = specs
            .flatMap { listOf(it.sensor.y, it.beacon.y, it.sensor.y + 1, it.beacon.y + 1) }
            .sorted()
            .toList()

        specs.forEach { spec ->
            val (sensor, beacon) = spec
            terrain[Position(xs.indexOf(sensor.x), ys.indexOf(sensor.y))] = Terrain.Sensor
            terrain[Position(xs.indexOf(beacon.x), ys.indexOf(beacon.y))] = Terrain.Beacon
            ys.indices.forEach { iy ->
                xs.indices.forEach { ix ->
                    val p = Position(xs[ix], ys[iy])
                    val ip = Position(ix, iy)
                    val existing = terrain[ip]
                    if (existing == Terrain.Free || existing == null) {
                        if (p.distance(sensor) <= spec.distance) {
                            terrain[ip] = Terrain.Empty
                        } else {
                            terrain[ip] = Terrain.Free
                        }
                    }
                }
            }
        }

        val freeTerrain = terrain
            .filter { it.value is Terrain.Free }
            .map { entry ->
                val (ix, iy) = entry.key
                Position(xs[ix], ys[iy])
            }
        println(freeTerrain.filter { it.x in (2911360..2911380) })
        return freeTerrain
            .first { (x, y) -> x in (0..max) && y in (0..max) }
    }

    fun findDistressBeacon2(input: Sequence<String>, max: Int): Position {
        val range = 0..max
        val specs = input
            .map { it.parseSpec() }
            .toList()
        val candidates = specs
            .asSequence()
            .flatMap { spec -> spec.candidates() }
            .filter { (x, y) -> x in range && y in range }
            .withIndex()
            .filter { p -> specs.all { it.sensor.distance(p.value) > it.distance } }
        val first = candidates.first()
        println(first.index)
        return first.value
    }


    fun solve1(input: Sequence<String>, y: Int = 10): Int {
        val world = buildWorld(input, y)
        return world.terrain
            .asSequence()
            .filter { it.key.y == y }
            .filter { it.value is Terrain.Empty }
            .count()
    }

    fun solve2(input: Sequence<String>, max: Int): Long {
        val p = findDistressBeacon2(input, max)
        println(p)
        return p.x * 4000000L + p.y
    }

    fun solve2Bis(input: Sequence<String>, max: Int): Long {
        val specs = input
            .map { it.parseSpec() }
            .toList()
        val (x, y) = (0 until max)
            .asSequence()
            .withIndex()
            .mapNotNull { findFreeX(it.value, specs, max)?.let { x -> x to it.index } }
            .first()
        println("x: $x y: $y")
        return x * 4000000L + y
    }

    fun findFreeX(y: Int, specs: List<SensorSpec>, max: Int): Int? {
        val segments = specs
            .asSequence()
            .filter { spec -> (spec.sensor.y - y).absoluteValue <= spec.distance }
            .map { spec ->
                val deltaX = spec.distance - (spec.sensor.y - y).absoluteValue
                val x1 = spec.sensor.x - deltaX
                val x2 = spec.sensor.x + deltaX
                x1 to x2
            }
            .filter { it.first <= max }
            .toList()
        val sorted = segments
            .asSequence()
            .flatMap { it.toList() }
            .sorted()
            .toList()
        val freeSpaces = Array(sorted.size) { true }
        segments.forEach { (x1, x2) ->
            val startIdx = sorted.binarySearch { it - x1 }
            val endIdx = sorted.binarySearch { it - x2 }
            (startIdx until endIdx).forEach {
                freeSpaces[it] = false
            }
        }
        val i = freeSpaces.indexOf(true)
        if (i == -1) {
            return null
        }
        return (sorted[i] + 1).takeIf { it in (0..max) }
    }
}
