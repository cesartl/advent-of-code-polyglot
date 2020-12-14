package com.ctl.aoc.kotlin.utils

fun <T> timed(f: () -> T): Pair<Double, T>{
    val start = System.nanoTime()
    val r = f()
    val end = System.nanoTime()
    return (end - start) / 1000.0 to r
}