package com.ctl.aoc.kotlin.y2024

object Day19 {

    data class TowelSpec(
        val patterns: List<String>,
        val designs: List<String>
    ) {
        val regex = patterns.joinToString(separator = "|", prefix = "(", postfix = ")+")
            .toRegex()

        private val cache = mutableMapOf<String, Long>()

        fun countWays(design: String): Long {
            val cached = cache[design]
            if (cached != null) {
                return cached
            }
            if (design.isEmpty()) {
                return 1
            }
            val count = patterns.asSequence()
                .filter { pattern -> design.startsWith(pattern) }
                .map { pattern -> design.removePrefix(pattern) }
                .map { pattern -> countWays(pattern) }
                .sum()
            cache[design] = count
            return count
        }
    }

    fun solve1(input: String): Int {
        val spec = input.parseTowelSpec()
        return spec.designs.count { spec.regex.matches(it) }
    }

    fun solve2(input: String): Long {
        val spec = input.parseTowelSpec()
        return spec.designs.sumOf { spec.countWays(it) }
    }
}

private fun String.parseTowelSpec(): Day19.TowelSpec {
    val (top, bottom) = this.split("\n\n")
    val patterns = top.trim().splitToSequence(",").map { it.trim() }.toList()
    val designs = bottom.trim().lines()
    return Day19.TowelSpec(patterns, designs)
}
