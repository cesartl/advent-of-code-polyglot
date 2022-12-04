package com.ctl.aoc.kotlin.utils


fun IntRange.rangeIntersect(other: IntRange): IntRange? {
    val newFirst = kotlin.math.max(this.first, other.first)
    val newLast = kotlin.math.min(this.last, other.last)
    if (newFirst <= newLast) {
        return newFirst..newLast
    }
    return null
//    val (r1, r2) = listOf(this, other).sortedBy { it.first }
//    if (r1.last < r2.first) {
//        return null
//    }
//    val start = r2.first
//    val end = r1.last.coerceAtMost(r2.last)
//    return start..end
}

fun IntRange.includes(other: IntRange): Boolean {
    return this.first <= other.first && this.last >= other.last
}
