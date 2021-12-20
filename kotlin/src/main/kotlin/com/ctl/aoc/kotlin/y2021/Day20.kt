package com.ctl.aoc.kotlin.y2021

import com.ctl.aoc.kotlin.utils.Position

val extension = 3

object Day20 {
    data class Image(val pixels: Set<Position>) {
        val topLeft: Position by lazy {
            val minX = pixels.minOf { it.x }
            val minY = pixels.minOf { it.y }
            Position(minX, minY)
        }
        val bottomRight: Position by lazy {
            val maxX = pixels.maxOf { it.x }
            val maxY = pixels.maxOf { it.y }
            Position(maxX, maxY)
        }

        fun algIndex(p: Position): Int {
            val (x, y) = p
            return sequence {
                (y - 1..y + 1).forEach { dy ->
                    (x - 1..x + 1).forEach { dx ->
                        yield(Position(dx, dy))
                    }
                }
            }.map { if (pixels.contains(it)) "1" else "0" }.joinToString(separator = "").toInt(2)
        }
    }

    fun Sequence<String>.toImage(): Image {
        return this.flatMapIndexed { y: Int, line: String ->
            line.asSequence().mapIndexedNotNull { x, c -> if (c == '#') Position(x, y) else null }
        }.toSet().run { Image(this) }
    }

    data class Algorithm(val bits: Set<Int>)

    fun String.toAlgorithm(): Algorithm {
        return this.mapIndexedNotNull { i, c ->
            if (c == '#') i else null
        }.toSet().run { Algorithm(this) }
    }

    fun Image.print() {
        val (minX, minY) = this.topLeft
        val (maxX, maxY) = this.bottomRight
        (minY..maxY).forEach { y ->
            (minX..maxX).forEach { x ->
                if (pixels.contains(Position(x, y))) print('#') else print('.')
            }
            println()
        }
    }

    fun Image.enhance(algorithm: Algorithm): Image {
        val (minX, minY) = this.topLeft
        val (maxX, maxY) = this.bottomRight
        return sequence {
            (minY - extension..maxY + extension).forEach { y ->
                (minX - extension..maxX + extension).forEach { x ->
                    yield(Position(x, y))
                }
            }
        }.mapNotNull { p ->
            val index = this.algIndex(p)
            if (algorithm.bits.contains(index)) {
                p
            } else {
                null
            }
        }.filter { (x, y) -> x in (minX - extension..maxX + extension) && y in (minY - extension..maxY + extension) }
                .toSet().run { Image(this) }
    }

    fun Algorithm.enhance(image: Image, n: Int): Image {
        return generateSequence(image) { nextImage -> nextImage.enhance(this) }.drop(n).first()
    }

    fun Image.crop(n: Int): Image {
        val (minX, minY) = this.topLeft
        val (maxX, maxY) = this.bottomRight
        val xRange = (minX + n)..(maxX - n)
        val yRange = (minY + n)..(maxY - n)
        return pixels.filter { (x, y) -> x in xRange && y in yRange }.toSet().run { Image(this) }
    }

    fun solve1(input: Sequence<String>): Int {
        val algorithm = input.first().toAlgorithm()
        val image = input.drop(1).toImage()
//        image.print()
//        println()
//        println()
//        val enhanced1 = algorithm.enhance(image, 1)
//        val enhanced2 = algorithm.enhance(enhanced1, 1)
//        enhanced2.print()
//        println()
//        val cropped = enhanced2.crop(extension + 1)
//        cropped.print()
//        return cropped.pixels.size
        return algorithm.enhanceAndCrop(image, 2).pixels.size
    }

    fun Algorithm.enhanceAndCrop(image: Image, n: Int): Image {
        return generateSequence(image) { nextImage -> nextImage.enhance(this).enhance(this).crop(extension + 1) }.drop(n / 2).first()
    }

    fun solve2(input: Sequence<String>): Int {
        val algorithm = input.first().toAlgorithm()
        val image = input.drop(1).toImage()
        return algorithm.enhanceAndCrop(image, 50).pixels.size
    }
}