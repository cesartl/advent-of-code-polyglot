package com.ctl.aoc.kotlin.y2015

object Day2 {
    data class Present(val dimensions: List<Int>) {
        private val sorted: List<Int> by lazy {
            dimensions.sorted()
        }
        val area = 2 * (dimensions[0] * dimensions[1] + dimensions[1] * dimensions[2] + dimensions[2] * dimensions[0])
        val extra: Int by lazy {
            sorted[0] * sorted[1]
        }
        val total = area + extra

        val ribbon: Int by lazy {
            2 * sorted[0] + 2 * sorted[1] + dimensions.fold(1) { acc, i -> i * acc }
        }

        companion object {
            fun parse(string: String): Present = Present(string.split("x").map { it.toInt() })
        }
    }

    fun solve1(lines: Sequence<String>): Int = lines.map { Present.parse(it) }.map { it.total }.sum()
    fun solve2(lines: Sequence<String>): Int = lines.map { Present.parse(it) }.map { it.ribbon }.sum()


}