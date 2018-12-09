package com.ctl.aoc.kotlin.utils


data class CircularLinkedList<T>(val value: T, private var previous: CircularLinkedList<T>?, private var next: CircularLinkedList<T>?) {

    val previousNode = previous ?: this
    val nextNode = next ?: this

    fun insert(value: T) {
        val node = of(value)
        val next = this.nextNode
        this.next = node
        node.previous = this
        node.next = next
        next.previous = node
    }

    fun removeNext(): T {
        val next = this.nextNode
        this.next = next.nextNode
        this.nextNode.previous = this
        return next.value
    }

    fun removePrevious(): T {
        val previous = this.previousNode
        this.previous = previous.previousNode
        this.previousNode.previous = this
        return previous.value

    }

    companion object {
        fun <T> of(value: T): CircularLinkedList<T> = CircularLinkedList(value, null, null)
    }
}