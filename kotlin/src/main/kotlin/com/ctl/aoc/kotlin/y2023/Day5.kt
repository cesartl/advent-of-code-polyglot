package com.ctl.aoc.kotlin.y2023

data class GardeningMapEntry(
    val destinationStart: Long,
    val sourceStart: Long,
    val length: Long
) {

    private val sourceRange = sourceStart until (sourceStart + length)
    fun mapsTo(source: Long): Long? {
        if (!sourceRange.contains(source)) {
            return null
        }
        return destinationStart + (source - sourceStart)
    }
}

data class GardeningMap(
    val entries: List<GardeningMapEntry>
) {

    private val sortedEntries = entries.sortedBy { it.sourceStart }

    fun mapsTo(source: Long): Long {
        return sortedEntries.asSequence()
            .mapNotNull { it.mapsTo(source) }
            .firstOrNull()
            ?: source
    }
}

data class GardenSpec(
    val seeds: List<Long>,
    val maps: List<GardeningMap>
)


private fun String.parseGardenSpec(): GardenSpec {
    val split = this.trim().split("\n\n")
    val seeds = split[0]
        .split(":")[1]
        .split(" ")
        .filterNot { it.isEmpty() }
        .map { it.toLong() }

    val maps = split
        .drop(1)
        .map { block ->
            val entries = block
                .splitToSequence("\n")
                .drop(1)
                .map { entry ->
                    val parts = entry.split(" ").map { it.toLong() }
                    GardeningMapEntry(
                        destinationStart = parts[0],
                        sourceStart = parts[1],
                        length = parts[2]
                    )
                }
                .toList()
            GardeningMap(entries)
        }
    return GardenSpec(seeds, maps)
}

object Day5 {
    fun solve1(input: String): Long {
        val (seeds, maps) = input.parseGardenSpec()
        val destinations = seeds.map { getDestination(it, maps) }
        return destinations.min()
    }

    private fun getDestination(seed: Long, maps: List<GardeningMap>): Long {
        return maps.fold(seed) { acc, map -> map.mapsTo(acc) }
    }

    fun solve2(input: String): Long {
        val (seeds, maps) = input.parseGardenSpec()
        val destinations = seeds
            .chunked(2)
            .asSequence()
            .flatMap {
                val start = it[0]
                val length = it[1]
                (start until start + length).asSequence().map { getDestination(it, maps) }
            }
        return destinations.min()
    }
}
