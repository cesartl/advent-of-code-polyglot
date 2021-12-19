package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.Position3d
import com.ctl.aoc.kotlin.utils.allRotations
import java.util.*
import kotlin.collections.ArrayDeque

object Day19 {

    data class Delta(val map: Map<Position3d, Pair<Position3d, Position3d>>)

    data class Scanner(val id: Int, val beacons: Set<Position3d>, val rotationId: Int = 0) {
        val deltas: Delta by lazy {
            val deltas = mutableMapOf<Position3d, Pair<Position3d, Position3d>>()
            beacons.forEach { b1 ->
                beacons.forEach { b2 ->
                    if (b1 != b2) {
                        deltas[b1 - b2] = b1 to b2
                    }
                }
            }
            Delta(deltas)
        }
    }

    fun Scanner.allScannerRotations(): Sequence<Scanner> {
        val scanner = this
        return allRotations().mapIndexed { idx, rotation ->
            scanner.copy(rotationId = idx, beacons = scanner.beacons.map { rotation(it) }.toSet())
        }
    }

    fun Scanner.offset(offset: Position3d): Scanner {
        return copy(beacons = beacons.map { it + offset }.toSet())
    }

    fun Scanner.findMatch(other: Scanner): Pair<Scanner, Position3d>? {
        return other.allScannerRotations()
                .mapNotNull { candidate -> this.isMatch(candidate)?.let { candidate to it } }
                .firstOrNull()
    }

    fun Scanner.isMatch(other: Scanner): Position3d? {
        if (this.id == other.id) {
            error("same scanner")
        }
        val intersect = this.deltas.map.keys.intersect(other.deltas.map.keys)
        if (intersect.size >= 12 * 11) {
            val offset = intersect.first().let { this.deltas.map[it]!!.first - other.deltas.map[it]!!.first }
            val check1 = intersect.all { this.deltas.map[it]!!.first - other.deltas.map[it]!!.first == offset }
            val check2 = intersect.all { this.deltas.map[it]!!.second - other.deltas.map[it]!!.second == offset }
            if (check1 && check2) {
                return offset
            } else {
                return null
            }
        }
        return null
    }

    data class Resolved(val offsetById: Map<Int, Position3d>, val scannerById: Map<Int, Scanner>) {
        val allBeacons: Set<Position3d> by lazy {
            scannerById.values.flatMap { it.offset(offsetById[it.id]!!).beacons }.toSet()
        }
    }

    fun solve1(input: String): Int {
        val resolved = resolve(input)
        return resolved.allBeacons.size
    }

    private fun resolve(input: String): Resolved {
        val parsed = input.parse()
        val s0 = parsed[0]
        val others = parsed
                .drop(1)
                .toList()
        val offsetById = mutableMapOf<Int, Position3d>()
        val scannerById = mutableMapOf<Int, Scanner>()
        offsetById[0] = Position3d(0, 0, 0)
        scannerById[0] = s0

        val unknown = ArrayDeque<Scanner>()
        others.forEach { unknown.addLast(it) }
        while (unknown.isNotEmpty()) {
            val current = unknown.removeFirst()
//            println("Doing ${current.id}")
            val match = scannerById.values
                    .mapNotNull { known -> known.findMatch(current)?.let { known to it } }
                    .firstOrNull()
            if (match != null) {
                val (known, matchResult) = match
                val (found, offset) = matchResult
                assert(found.id == current.id)
                val newOffset = offsetById[known.id]!! + offset
                offsetById[found.id] = newOffset
                scannerById[found.id] = found
            } else {
                unknown.addLast(current)
            }
        }
        return Resolved(offsetById, scannerById)
    }

    fun solve2(input: String): Int {
        val resolved = resolve(input)
        return sequence {
            resolved.offsetById.values.forEach { s1 ->
                resolved.offsetById.values.forEach { s2 ->
                    if (s1 != s2) {
                        yield(s1.distance(s2))
                    }
                }
            }
        }.maxOrNull()!!
    }

    fun String.parse(): List<Scanner> {
        return this.split("\n\n").map { data ->
            val lines = data.split("\n")
            val id = """--- scanner ([\d]+) ---""".toRegex().matchEntire(lines[0])!!.groupValues[1].toInt()
            val beacons = lines.drop(1)
                    .map { it.split(",") }
                    .map { (x, y, z) -> Position3d(x.toInt(), y.toInt(), z.toInt()) }
                    .toSet()
            Scanner(id, beacons)
        }
    }

}