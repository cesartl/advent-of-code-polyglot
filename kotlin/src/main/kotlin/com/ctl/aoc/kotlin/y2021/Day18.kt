package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.y2021.Day18.TreeSnail.PairNumber
import com.ctl.aoc.kotlin.y2021.Day18.TreeSnail.RegularNumber

object Day18 {

    fun solve1Tree(input: Sequence<String>): Int {
        val numbers = input.map { it.toTreeSnail() }
        val first = numbers.first()
        val result = numbers.drop(1).fold(first) { acc, next -> acc + next }
        return result.magnitude()
    }

    fun solve1Flat(input: Sequence<String>): Int {
        val numbers = input.map { it.toFlatSnail() }
        val first = numbers.first()
        val result = numbers.drop(1).fold(first) { acc, next -> acc + next }
        return result.magnitude
    }

    fun solve2Tree(input: Sequence<String>): Int {
        val numbers = input.map { it.toTreeSnail() }
        return sequence {
            numbers.forEach { n1 ->
                numbers.forEach { n2 ->
                    if (n1 != n2) {
                        yield(n1 + n2)
                    }
                }
            }
        }.maxOf { it.magnitude() }
    }


    fun solve2Flat(input: Sequence<String>): Int {
        val numbers = input.map { it.toFlatSnail() }
        return sequence {
            numbers.forEach { n1 ->
                numbers.forEach { n2 ->
                    if (n1 != n2) {
                        yield(n1 + n2)
                    }
                }
            }
        }.maxOf { it.magnitude }
    }


    /**
     * Tree representation of a Snail number
     */
    sealed class TreeSnail {
        data class RegularNumber(val n: Int) : TreeSnail() {
            override fun toString(): String = this.n.toString()
        }

        data class PairNumber(val left: TreeSnail, val right: TreeSnail) : TreeSnail() {
            override fun toString(): String = "[$left,$right]"
        }
    }

    /**
     * Method to apply a set of change to the tree. Pair<Int, Int> corresponds to (index, diff)
     */
    private fun TreeSnail.applyDiffs(indexOffset: Int, vararg diffs: Pair<Int, Int>): Pair<TreeSnail, Int> {
        return when (this) {
            is RegularNumber -> {
                return (diffs.find { (index, _) -> index == indexOffset }?.let { (_, diff) ->
                    RegularNumber(n = this.n + diff)
                } ?: this) to (indexOffset + 1)
            }
            is PairNumber -> {
                val (left, right) = this
                val (newLeft, leftOffset) = left.applyDiffs(indexOffset, *diffs)
                val (newRight, rightOffset) = right.applyDiffs(leftOffset, *diffs)
                PairNumber(newLeft, newRight) to rightOffset
            }
        }
    }

    data class ExplodeMatch(val index: Int, val left: Int, val right: Int)
    data class ExplodeResult(val node: TreeSnail, val indexOffset: Int, val match: ExplodeMatch? = null)

    /**
     * Search for a node in tree where explode should be applied. If explode match found, result will contain a ExplodeMatch
     */
    private fun TreeSnail.explodeSearch(depth: Int = 0, indexOffset: Int): ExplodeResult {
        return if (depth == 4) {
            when (this) {
                is PairNumber -> {
                    val leftN = (this.left as RegularNumber).n
                    val rightN = (this.right as RegularNumber).n
                    ExplodeResult(RegularNumber(0), indexOffset, ExplodeMatch(indexOffset, leftN, rightN))
                }
                is RegularNumber -> ExplodeResult(this, indexOffset + 1)
            }
        } else {
            when (this) {
                is RegularNumber -> ExplodeResult(this, indexOffset + 1)
                is PairNumber -> {
                    //search to the left first
                    val leftSearch = left.explodeSearch(depth + 1, indexOffset)
                    if (leftSearch.match != null) {
                        return leftSearch.copy(node = this.copy(left = leftSearch.node))
                    }
                    //if not found, search to the right
                    val rightSearch = right.explodeSearch(depth + 1, leftSearch.indexOffset)
                    return rightSearch.copy(node = this.copy(right = rightSearch.node))
                }
            }
        }
    }

    /**
     * Result._2 is true if explode was applied
     */
    fun TreeSnail.explode(): Pair<TreeSnail, Boolean> {
        val result = this.explodeSearch(0, 0)
        return result.match?.let { (index, leftN, rightN) ->
            result.node.applyDiffs(0, index - 1 to leftN, index + 1 to rightN).first to true
        } ?: (this to false)
    }

    fun Int.split(): Pair<Int, Int> {
        val d = this / 2
        val r = this % 2
        return d to d + r
    }

    fun TreeSnail.split(): Pair<TreeSnail, Boolean> {
        return when (this) {
            is RegularNumber -> {
                if (this.n >= 10) {
                    val (l, r) = this.n.split()
                    PairNumber(RegularNumber(l), RegularNumber(r)) to true
                } else {
                    this to false
                }
            }
            is PairNumber -> {
                val (newLeft, leftSplit) = left.split()
                if (leftSplit) {
                    return this.copy(left = newLeft) to true
                }
                val (newRight, rightSplit) = right.split()
                return this.copy(right = newRight) to rightSplit
            }
        }
    }

    private tailrec fun TreeSnail.reduce(): TreeSnail {
        val (afterExplode, isExplode) = this.explode()
        return if (isExplode) {
            afterExplode.reduce()
        } else {
            val (afterSplit, isSplit) = this.split()
            if (isSplit) {
                afterSplit.reduce()
            } else {
                this
            }
        }
    }

    operator fun TreeSnail.plus(other: TreeSnail): TreeSnail {
        return PairNumber(this, other).reduce()
    }

    fun String.toTreeSnail() = TreeSnailParser(this).parse()

    private fun TreeSnail.magnitude(): Int = when (this) {
        is RegularNumber -> this.n
        is PairNumber -> 3 * this.left.magnitude() + 2 * this.right.magnitude()
    }


    //------- Flat representation

    data class SnailElement(val value: Int, val depth: Int) {
        override fun toString(): String {
            return "(value=$value, depth=$depth)"
        }

        fun increaseDepth(): SnailElement = this.copy(depth = depth + 1)

        fun addValue(toAdd: Int): SnailElement = this.copy(value = value + toAdd)
        fun explode(): SnailElement = this.copy(value = 0, depth = depth - 1)
        fun split(): Pair<SnailElement, SnailElement> {
            val (left, right) = value.split()
            val newDepth = depth + 1
            return SnailElement(left, newDepth) to SnailElement(right, newDepth)
        }
    }

    /**
     * Flat representation, basically just a list of (value,depth)
     */
    data class FlatSnail(val elements: List<SnailElement>) {

        val magnitude: Int by lazy {
            snailTree.magnitude()
        }

        override fun toString(): String = elements.toString()

        /**
         * tree representation
         */
        val snailTree: TreeSnail by lazy {
            val stack = ArrayDeque<Pair<TreeSnail, Int>>()
            //going left to right
            elements.forEach { (value, depth) ->
                //adding the current element as a leaf in the stack
                stack.addFirst(RegularNumber(value) to depth)

                //we keep combining into pairs while the top 2 elements have the same depth
                var continueReducing = true
                while (stack.size > 1 && continueReducing) {
                    val right = stack.removeFirst()
                    continueReducing = right.second == stack.first().second
                    if (continueReducing) {
                        val left = stack.removeFirst()
                        stack.addFirst(PairNumber(left.first, right.first) to left.second - 1)
                    } else {
                        //adding back what we removed by mistake
                        stack.addFirst(right)
                    }
                }
            }
            assert(stack.size == 1) { "Not a valid tree" }
            stack.first().first
        }

        operator fun plus(other: FlatSnail): FlatSnail {
            val combined = mutableListOf<SnailElement>()
            combined.addAll(this.elements.map { it.increaseDepth() })
            combined.addAll(other.elements.map { it.increaseDepth() })

            //reducing logic
            var continueReduce = true
            while (continueReduce) {
                if (explode(combined)) {
                    continue
                }
                if (split(combined)) {
                    continue
                }
                continueReduce = false
            }
            return FlatSnail(combined)
        }

        //trivial :D
        fun explode(mutableElements: MutableList<SnailElement>): Boolean {
            val target = mutableElements.indexOfFirst { it.depth > 4 }
            if (target < 0) {
                return false
            }

            val left = mutableElements[target]
            val right = mutableElements[target + 1]

            if (target > 0) {
                mutableElements[target - 1] = mutableElements[target - 1].addValue(left.value)
            }
            if (target < mutableElements.size - 2) {
                mutableElements[target + 2] = mutableElements[target + 2].addValue(right.value)
            }

            mutableElements[target] = left.explode()
            mutableElements.removeAt(target + 1)
            return true
        }


        //trivial :D
        fun split(mutableElements: MutableList<SnailElement>): Boolean {
            val target = mutableElements.indexOfFirst { it.value >= 10 }

            if (target < 0) {
                return false
            }

            val (left, right) = mutableElements[target].split()
            mutableElements[target] = left
            mutableElements.add(target + 1, right)

            return true
        }
    }

    /**
     * Explode and rebuild the FlatSnail, used for testing only
     */
    fun FlatSnail.explode(): FlatSnail? {
        val mutableElements = elements.toMutableList()
        this.explode(mutableElements)
        return FlatSnail(mutableElements)
    }

    /**
     * Split and rebuild the FlatSnail, used for testing only
     */
    fun FlatSnail.split(): FlatSnail? {
        val mutableElements = elements.toMutableList()
        this.split(mutableElements)
        return FlatSnail(mutableElements)
    }

    // parsing
    data class TreeSnailParser(val source: String) {
        var start = 0
        var current = 0

        fun parse(): TreeSnail {
            start = current
            val c = advance()
            return when {
                c == '[' -> {
                    val left = parse()
                    assert(advance() == ',')
                    val right = parse()
                    val pair = PairNumber(left, right)
                    assert(advance() == ']')
                    pair
                }
                c.isDigit() -> {
                    while (peek().isDigit()) {
                        advance()
                    }
                    RegularNumber(source.substring(start, current).toInt())
                }
                else -> {
                    error("Unknown car $c")
                }
            }
        }

        private fun advance(): Char = source[current++]
        private fun peek(): Char = source[current]
    }

    fun String.toFlatSnail(): FlatSnail {
        var depth = 0
        val elements = mutableListOf<SnailElement>()
        var idx = 0
        while (idx < this.length) {
            val c = this[idx]
            when {
                c == '[' -> {
                    depth++
                    idx++
                }
                c == ']' -> {
                    depth--
                    idx++
                }
                c.isDigit() -> {
                    val start = idx
                    while (this[idx].isDigit()) {
                        idx++
                    }
                    elements.add(SnailElement(this.substring(start, idx).toInt(), depth))
                }
                else -> idx++
            }
        }
        return FlatSnail(elements)
    }
}