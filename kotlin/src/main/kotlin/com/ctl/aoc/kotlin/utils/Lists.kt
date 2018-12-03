package com.ctl.aoc.kotlin.utils

val <T> List<T>.tail: List<T>
    get() = drop(1)

val <T> List<T>.head: T
    get() = first()

object Lists {
    fun <T> weave(first: List<T>, second: List<T>, prefix: List<T> = listOf()): List<List<T>> {
        if (first.isEmpty()) {
            return listOf(prefix + second)
        }
        if (second.isEmpty()) {
            return listOf(prefix + first)
        }
        return weave(first.tail, second, prefix + first.head) + weave(first, second.tail, prefix + second.head)
    }
}