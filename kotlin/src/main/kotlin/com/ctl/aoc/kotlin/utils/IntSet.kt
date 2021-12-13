package com.ctl.aoc.kotlin.utils

@JvmInline
value class IntSet(private val bits: Int = 0) {
    internal fun containsInternal(index: Int): Boolean {
        return ((1 shl index) and bits) > 0
    }

    internal fun addInternal(index: Int): IntSet {
        return IntSet(bits or (1 shl index))
    }

    internal fun removeInternal(index: Int): IntSet {
        return IntSet(bits and (1 shl index).inv())
    }

    internal fun isEmpty(): Boolean = bits == 0

    internal fun size(): Int = Integer.toBinaryString(bits).count { it == '1' }
}

class IntSetMapper<T>() {
    private val dictionary: MutableMap<T, Int> = mutableMapOf()
    private var index = 0
    fun add(word: T): IntSetMapper<T> {
        if (index >= 31) {
            error("Too many words. Maximum 32")
        }
        dictionary[word] = index++
        return this
    }

    fun build(): IntSetImpl<T> = IntSetImpl(IntSet(), this)

    fun IntSet.add(element: T): IntSet {
        return this.addInternal(elementIndex(element))
    }

    fun IntSet.remove(element: T): IntSet {
        return this.removeInternal(elementIndex(element))
    }

    fun IntSet.contains(element: T): Boolean {
        return this.containsInternal(elementIndex(element))
    }

    private fun elementIndex(element: T) = dictionary[element] ?: error("Unknown word $element")
}

data class IntSetImpl<T>(val intSet: IntSet, val mapper: IntSetMapper<T>) : Set<T> {
    override val size: Int
        get() = intSet.size()

    override fun contains(element: T): Boolean = mapper.run { intSet.contains(element) }

    override fun containsAll(elements: Collection<T>) = elements.all { contains(it) }

    override fun isEmpty(): Boolean = intSet.isEmpty()

    override fun iterator(): Iterator<T> {
        TODO("Not yet implemented")
    }

    fun add(element: T): IntSetImpl<T> = copy(intSet = mapper.run { intSet.add(element) })
}
