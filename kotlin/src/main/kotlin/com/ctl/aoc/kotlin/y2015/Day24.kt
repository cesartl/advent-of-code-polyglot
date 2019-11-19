package com.ctl.aoc.kotlin.y2015

object Day24 {
    fun solve1(packages: List<Long>): Long {
        val groups = buildGroups(packages.reversed(), 3)
        val firstGroup: List<Long>? = groups.minWith(comparator)
        return firstGroup?.let { qc(it) } ?: 0
    }

    fun solve2(packages: List<Long>): Long {
        val groups = buildGroups(packages.reversed(), 4)
        val firstGroup: List<Long>? = groups.minWith(comparator)
        return firstGroup?.let { qc(it) } ?: 0
    }

    private fun qc(list: List<Long>): Long = list.reduce { x, y -> x * y }

    private val comparator: Comparator<List<Long>> = Comparator { p0, p1 ->
        when {
            p0.size != p1.size -> p0.size - p1.size
            else -> (qc(p0) - qc(p1)).toInt()
        }
    }

    fun buildGroups(packages: List<Long>, n: Long): List<List<Long>> {
        val targetSum = packages.sum() / n
        val results = mutableListOf<List<Long>>()

        var candidates = mutableListOf<List<Long>>()
        candidates.add(listOf())

        packages.forEach { pkg ->
            val newCandidates = mutableListOf<List<Long>>()
            candidates.forEach { c ->
                newCandidates.add(c)

                val soFar = c.sum()
                if (soFar + pkg <= targetSum) {
                    newCandidates.add(c + pkg)
                }
            }
            candidates = newCandidates
        }
        return candidates.filter { it.sum() == targetSum }
    }
}