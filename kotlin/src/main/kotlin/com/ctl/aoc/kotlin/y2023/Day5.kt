package com.ctl.aoc.kotlin.y2023

data class GardeningMapEntry(
    val destinationStart: Long,
    val sourceStart: Long,
    val length: Long
) {

    val sourceRange = sourceStart until (sourceStart + length)

    fun contains(source: Long): Boolean {
        return sourceRange.contains(source)
    }

    fun mapsTo(source: Long): Long? {
        if (!sourceRange.contains(source)) {
            return null
        }
        return destinationStart + (source - sourceStart)
    }

    private fun shiftStart(offset: Long): GardeningMapEntry {
        val newSourceStart = sourceStart + offset
        val newDestinationStart = destinationStart + offset
        return GardeningMapEntry(
            destinationStart = newDestinationStart,
            sourceStart = newSourceStart,
            length = length - offset
        )
    }

    fun startSourceAt(newSourceStart: Long): GardeningMapEntry {
        return shiftStart(newSourceStart - sourceStart)
    }
}

data class GardeningMap(
    val entries: List<GardeningMapEntry>
) {

    fun mapsTo(source: Long): Long {
        return findMatchingEntry(source)?.mapsTo(source) ?: error("no map for $source")
    }

    private fun findMatchingEntry(source: Long): GardeningMapEntry? {
        return this.entries.firstOrNull { it.contains(source) }
    }

    fun remapRanges(range: LongRange): Sequence<LongRange> {
        val subEntries = entries.asSequence()
            .dropWhile { !it.contains(range.first) }
            .takeWhile { it.sourceRange.first <= range.last }
            .toMutableList()

        subEntries[0] = subEntries.first().startSourceAt(range.first)
        val last = subEntries.last()
        subEntries[subEntries.size - 1] = last.copy(length = range.last - last.sourceStart + 1)

        return subEntries
            .asSequence()
            .map { it.destinationStart until it.destinationStart + it.length }
    }
}

data class GardenSpec(
    val seeds: List<Long>,
    val maps: List<GardeningMap>
)


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
        val initialRanges = seeds
            .chunked(2)
            .asSequence()
            .map {
                val start = it[0]
                val length = it[1]
                start until (start + length)
            }.toList()
        val finalRanges = maps.fold(initialRanges) { acc, map -> acc.flatMap { map.remapRanges(it) } }
        return finalRanges
            .asSequence()
            .map { it.first }
            .filter { it > 0 }
            .min()
    }
}

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
                }.sortedBy { it.sourceStart }
                .toList()

            val firstEntry = entries.first()
            val initial = if (firstEntry.sourceStart != 0L) {
                val firstGap = GardeningMapEntry(
                    destinationStart = 0,
                    sourceStart = 0,
                    length = firstEntry.sourceStart
                )
                listOf(firstGap)
            } else {
                listOf()
            }

            val withGaps = entries
                .zipWithNext()
                .fold(initial) { acc, (current, next) ->
                    if (current.sourceRange.last + 1 == next.sourceRange.first) {
                        acc + listOf(current, next)
                    } else {
                        val start = current.sourceRange.last + 1
                        val length = next.sourceRange.first - current.sourceRange.last
                        val gap = GardeningMapEntry(destinationStart = start, sourceStart = start, length = length)
                        acc + listOf(current, gap, next)
                    }
                }
            val last = withGaps.last().sourceRange.last
            GardeningMap(
                withGaps + listOf(
                    GardeningMapEntry(
                        destinationStart = last + 1,
                        sourceStart = last + 1,
                        length = Long.MAX_VALUE - last - 1
                    )
                )
            )
        }
    return GardenSpec(seeds, maps)
}
