package com.ctl.aoc.kotlin.y2023

import com.ctl.aoc.kotlin.utils.*

fun String.hash(): Int {
    return this.fold(0) { acc, c ->
        (acc + c.code) * 17 % 256
    }
}

data class Lens(
    val label: String,
    val focal: Int
)


class HashBox {
    private var head: LinkedList<Lens> = Nil

    fun put(label: String, focal: Int) {
        val newLens = Lens(label, focal)
        head = if (head.any { it.label == label }) {
            replace(newLens)
        } else {
            head.append(newLens)
        }
    }

    fun remove(label: String) {
        head = head.foldRight(Nil as LinkedList<Lens>) { lens, node ->
            if (lens.label == label) {
                node
            } else {
                Cons(lens, node)
            }
        }
    }

    private fun replace(newLens: Lens): LinkedList<Lens> {
        return head.foldRight(Nil as LinkedList<Lens>) { lens, node ->
            if (lens.label == newLens.label) {
                Cons(newLens, node)
            } else {
                Cons(lens, node)
            }
        }
    }

    fun focusPower(): Int {
        return head.foldLeftIndexed(0) { i, acc, l ->
            acc + (i + 1) * l.focal
        }
    }

    override fun toString(): String {
        return "HashBox($head)"
    }


}

class Lenses(
    private val boxes: Array<HashBox> = Array(256) { HashBox() }
) {

    fun focusPower(): Int {
        return boxes.foldIndexed(0) { i, acc, box ->
            acc + (i + 1) * box.focusPower()
        }
    }

    private fun put(label: String, focal: Int) {
        val box = boxes[label.hash()]
        box.put(label, focal)
    }

    fun remove(label: String) {
        val box = boxes[label.hash()]
        box.remove(label)
    }

    fun apply(op: String) {
        if (op.contains("=")) {
            val s = op.split("=")
            val label = s[0]
            val focal = s[1].toInt()
            this.put(label, focal)
        } else {
            assert(op.contains("-"))
            val s = op.split("-")
            val label = s[0]
            this.remove(label)
        }
    }
}

object Day15 {
    fun solve1(input: String): Int {
        return input.splitToSequence(",")
            .map { it.trim() }
            .map { it.hash() }
            .sum()
    }

    fun solve2(input: String): Int {
        val lenses = Lenses()
        input.splitToSequence(",")
            .map { it.trim() }
            .forEach { op ->
                lenses.apply(op)
            }
        return lenses.focusPower()
    }
}
