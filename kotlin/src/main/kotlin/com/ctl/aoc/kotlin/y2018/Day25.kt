package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.Coordinate4D

typealias Constellation = MutableList<Coordinate4D>

object Day25 {

    fun parse(line: String): Coordinate4D {
        val es = line.split(",").toList()
        return Coordinate4D(es[0].toInt(), es[1].toInt(), es[2].toInt(), es[3].toInt())
    }

    fun cluster(points: Sequence<Coordinate4D>): List<Constellation> {
        val constellations = mutableListOf<Constellation>()


        var current = mutableListOf<Coordinate4D>()

        var remaining = points.toList()

        while (remaining.isNotEmpty()) {
            if (current.isEmpty()) {
                current.add(remaining.first())
                remaining = remaining.drop(1)
            } else {
                val (inCluster, outCluster) = remaining.partition { r -> current.any { it.distance(r) <= 3 } }
                if (inCluster.isNotEmpty()) {
                    current.addAll(inCluster)
                    remaining = outCluster
                } else {
                    constellations.add(current)
                    current = mutableListOf()
                }
            }
        }
        if (current.isNotEmpty()) {
            constellations.add(current)
        }

        return constellations
    }

    fun solve1(lines: Sequence<String>): Int {
        val points = lines.map { parse(it) }
        val constellations = cluster(points)
        return constellations.size
    }
}