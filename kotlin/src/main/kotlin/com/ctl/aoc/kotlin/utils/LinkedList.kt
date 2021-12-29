package com.ctl.aoc.kotlin.utils

sealed class LinkedList<out T> {
    override fun toString(): String = buildString().dropLast(1).toString()
}

object Nil : LinkedList<Nothing>() {}

data class Cons<T>(val head: T, val tail: LinkedList<T>) : LinkedList<T>() {
    override fun toString(): String {
        return super.toString()
    }
}

fun <T> LinkedList<T>.isEmpty(): Boolean = when (this) {
    is Cons -> false
    Nil -> true
}

fun <T> LinkedList<T>.first(): T = when (this) {
    is Cons -> head
    Nil -> error("Empty list")
}

tailrec fun <T> LinkedList<T>.drop(n: Int): LinkedList<T> {
    if (n == 0) return this
    return when (this) {
        Nil -> error("empty list")
        is Cons -> tail.drop(n - 1)
    }
}


fun <T> LinkedList<T>.buildString(): StringBuilder =
    this.foldLeft(StringBuilder()) { builder, n -> builder.append(n).append(",") }

fun <T> LinkedList<T>.add(t: T): LinkedList<T> {
    return Cons(t, this)
}

tailrec fun <T, R> LinkedList<T>.foldLeft(initial: R, operation: (acc: R, T) -> R): R = when (this) {
    Nil -> initial
    is Cons -> tail.foldLeft(operation(initial, head), operation)
}

tailrec fun <T, R> LinkedList<T>.foldLeftIndexed(initial: R, idx: Int = 0, operation: (idx: Int, acc: R, T) -> R): R =
    when (this) {
        Nil -> initial
        is Cons -> tail.foldLeftIndexed(operation(idx, initial, head), idx + 1, operation)
    }

fun <T> Iterable<T>.toLinkedList(): LinkedList<T> =
    this.reversed().fold(Nil as LinkedList<T>) { acc, t -> Cons(t, acc) }

fun <T> LinkedList<T>.any(predicate: (T) -> Boolean) = this.foldLeft(false) { acc, t -> acc || predicate(t) }
fun <T> LinkedList<T>.none(predicate: (T) -> Boolean) = this.foldLeft(true) { acc, t -> acc && !predicate(t) }
fun <T> LinkedList<T>.all(predicate: (T) -> Boolean) = this.foldLeft(true) { acc, t -> acc && predicate(t) }
