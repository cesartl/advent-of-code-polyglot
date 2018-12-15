package com.ctl.aoc.kotlin.utils

object Sequences {
    fun <T> startWith(s1: Sequence<T>, s2: Sequence<T>): Boolean {
        return s1.zip(s2).all { it.first == it.second }
    }
}

fun <T> Sequence<T>.startWith(other: Sequence<T>): Boolean = Sequences.startWith(this, other)