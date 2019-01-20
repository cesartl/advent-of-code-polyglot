package com.ctl.aoc.kotlin.y2015

object Day16 {
    data class Trait(val name: String, val n: Int) {
        companion object {
            val regex = """([\w]+): ([\d]+)""".toRegex()
            fun parse(s: String): Trait {
                val m = regex.matchEntire(s)
                if (m != null) {
                    return Trait(m.groupValues[1], m.groupValues[2].toInt())
                }
                throw IllegalArgumentException(s)
            }
        }
    }

    data class ScanResult(val traits: Map<String, Int>)

    data class AuntSue(val sue: Int, val traits: Map<String, Int>) {

        fun isCompatibleWithScan(scanResult: ScanResult): Boolean {
            return scanResult.traits.all { (name, n) -> traits[name] == null || traits[name] == n }
        }

        fun isCompatibleWithScan2(scanResult: ScanResult): Boolean {
            return scanResult.traits.all { (name, n) ->
                traits[name]?.let {
                    when (name) {
                        "cats", "trees" -> it > n
                        "pomeranians", "goldfish" -> it < n
                        else -> it == n
                    }
                } ?: true
            }
        }

        companion object {
            fun parse(line: String): AuntSue {
                val i = line.indexOf(":")
                val sue = line.substring(0 until i).split(" ")[1].toInt()
                val traits = line.substring(i + 1).split(",").map { Trait.parse(it.trim()) }
                return AuntSue(sue, traits.map { it.name to it.n }.toMap())
            }
        }
    }

    fun solve1(scanResult: ScanResult, lines: Sequence<String>): Int {
        val candidates = lines.map { AuntSue.parse(it) }.filter { it.isCompatibleWithScan(scanResult) }.toList()
        println(candidates)
        return candidates.firstOrNull()?.sue ?: 0
    }

    fun solve2(scanResult: ScanResult, lines: Sequence<String>): Int {
        val candidates = lines.map { AuntSue.parse(it) }.filter { it.isCompatibleWithScan2(scanResult) }.toList()
        println(candidates)
        return candidates.firstOrNull()?.sue ?: 0
    }

}