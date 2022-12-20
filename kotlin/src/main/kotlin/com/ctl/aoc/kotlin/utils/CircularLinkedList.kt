package com.ctl.aoc.kotlin.utils


data class CircularLinkedList<T>(val value: T, var previous: CircularLinkedList<T>?, var next: CircularLinkedList<T>?) {


    fun previousNode(): CircularLinkedList<T> = previous ?: this
    fun nextNode(): CircularLinkedList<T> = next ?: this

    fun previousNode(n: Int): CircularLinkedList<T> {
        var current = this
        for (i in 1..n) {
            current = current.previousNode()
        }
        return current
    }


    fun nextNode(n: Int): CircularLinkedList<T> {
        var current = this
        for (i in 1..n) {
            current = current.nextNode()
        }
        return current
    }

    fun insert(value: T): CircularLinkedList<T> {
        val node = of(value)
        val next = this.nextNode()
        this.next = node
        node.previous = this
        node.next = next
        next.previous = node
        return node
    }

    fun removeNext(): T {
        val next = this.nextNode()
        this.next = next.nextNode()
        this.nextNode().previous = this
        return next.value
    }

    fun removePrevious(): T {
        val previous = this.previousNode()
        this.previous = previous.previousNode()
        this.previousNode().next = this
        return previous.value

    }

    fun print(f: (CircularLinkedList<T>) -> String = { " ${it.value.toString()} " }): String {
        val builder = StringBuilder()
        var current = this
        while (builder.isEmpty() || current != this) {
            builder.append(f(current))
            current = current.nextNode()
        }
        return builder.toString()
    }

    override fun toString(): String {
        return "$value"
    }


    companion object {
        fun <T> of(value: T): CircularLinkedList<T> = CircularLinkedList(value, null, null)
    }
}
