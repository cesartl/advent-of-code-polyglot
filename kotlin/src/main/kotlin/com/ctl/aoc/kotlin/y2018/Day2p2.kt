package com.ctl.aoc.kotlin.y2018

object Day2p2 {

    fun solve(input: Sequence<String>): String {
        val list = input.toList()
        val candidates = mutableListOf<String>()
        var id: String?
        for (i in list.indices) {
            for (j in i + 1 until list.size) {
                id = findDifference(list[i], list[j])
                if (id != null) {
                    candidates.add(id)
                }
            }
        }
        if (candidates.size != 1) throw IllegalAccessException(candidates.joinToString())
        return candidates.first()
    }

    fun findDifference(s1: String, s2: String): String? {
        var differenceFoundAt = -1
        var c1: Char
        var c2: Char
        for (i in s1.indices) {
            if (s1[i] != s2[i]) {
                if (differenceFoundAt >= 0) return null
                differenceFoundAt = i
            }
        }
        return s1.removeRange(differenceFoundAt until differenceFoundAt + 1)
    }
}