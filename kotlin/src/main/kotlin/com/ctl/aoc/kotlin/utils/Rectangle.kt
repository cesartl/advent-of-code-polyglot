package com.ctl.aoc.kotlin.utils


interface Rectangle<T>
        where T : Number {
    val x: T
    val y: T
    val width: T
    val height: T

    operator fun Number.minus(other: Number): Number {
        return when (this) {
            is Long -> this.toLong() - other.toLong()
            is Int -> this.toInt() - other.toInt()
            is Short -> this.toShort() - other.toShort()
            is Byte -> this.toByte() - other.toByte()
            is Double -> this.toDouble() - other.toDouble()
            is Float -> this.toFloat() - other.toFloat()
            else -> throw RuntimeException("Unknown numeric type")
        }
    }

    operator fun Number.plus(other: Number): Number {
        return when (this) {
            is Long -> this.toLong() + other.toLong()
            is Int -> this.toInt() + other.toInt()
            is Short -> this.toShort() + other.toShort()
            is Byte -> this.toByte() + other.toByte()
            is Double -> this.toDouble() + other.toDouble()
            is Float -> this.toFloat() + other.toFloat()
            else -> throw RuntimeException("Unknown numeric type")
        }
    }

    operator fun Number.compareTo(other: Number): Int = this.minus(other).toInt()

    fun <T> valueInRange(value: T, min: T, max: T): Boolean
            where T : Number = value >= min && value <= max
}

fun <T> doRecsOverlap(a: Rectangle<T>, b: Rectangle<T>): Boolean
        where T : Number {
    val xOverlap = a.run { valueInRange(a.x, b.x, b.x + b.width - 1) || valueInRange(b.x, a.x, a.x + a.width - 1) }
    val yOverlap = a.run { valueInRange(a.y, b.y, b.y + b.height - 1) || valueInRange(b.y, a.y, a.y + a.height - 1) }
    return xOverlap && yOverlap
}


fun <T : Number> Rectangle<T>.overLapWith(other: Rectangle<T>): Boolean = doRecsOverlap(this, other)