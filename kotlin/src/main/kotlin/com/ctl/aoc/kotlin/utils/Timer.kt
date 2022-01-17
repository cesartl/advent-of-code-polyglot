package com.ctl.aoc.kotlin.utils

fun <T> timedNanos(f: () -> T): Pair<Double, T> {
    val start = System.nanoTime()
    val r = f()
    val end = System.nanoTime()
    return (end - start) / 1000.0 to r
}

fun <T> timedMs(f: () -> T): Pair<String, T> {
    val start = System.currentTimeMillis()
    val r = f()
    val end = System.currentTimeMillis()
    return "${end - start}ms" to r
}

fun <T> timed(f: () -> T): Pair<Long, T> {
    val start = System.currentTimeMillis()
    val r = f()
    val end = System.currentTimeMillis()
    return end - start to r
}