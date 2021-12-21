package com.ctl.aoc.kotlin.utils

fun <K, V> Map<K, V>.merge(other: Map<K, V>, merge: (V, V) -> V): Map<K, V> {
    return (this.asSequence() + other.asSequence())
            .groupBy({ it.key }, { it.value })
            .mapValues { e -> e.value.reduce(merge) }
            .toMap()
}