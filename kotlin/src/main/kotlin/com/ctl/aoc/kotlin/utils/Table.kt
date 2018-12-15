package com.ctl.aoc.kotlin.utils

data class MutableTable<T>(val map: MutableMap<Int, MutableMap<Int, T>>) {
    fun get(x: Int, y: Int): T? = map[y]?.get(x)

    fun put(x: Int, y: Int, t: T) {
        map.computeIfAbsent(y) { mutableMapOf() }[x] = t
    }

    fun remove(x: Int, y: Int) {
        map[y]?.remove(x)
    }

    fun all(): List<T> = map.entries.flatMap { row -> row.value.map { it.value } }

    companion object {
        fun <T> mutableTable(): MutableTable<T> = MutableTable(mutableMapOf())
    }
}