package com.ctl.aoc.kotlin.y2019

import com.ctl.aoc.kotlin.utils.frequency
import com.ctl.aoc.kotlin.utils.head

object Day8 {
    fun solve1(input: String, width: Int, height: Int): Int {
        val image = input.map { it.toInt() - '0'.toInt() }.toList()
        val layers = buildLayers(width, height, image)

        val layer = layers.map { it.frequency() }.minByOrNull { it[0] ?: 0 }!!
        return (layer[1] ?: 0) * (layer[2] ?: 0)
    }

    private fun buildLayers(width: Int, height: Int, image: List<Int>): List<List<Int>> {
        val layers = mutableListOf<List<Int>>()
        val size = width * height
        val n = image.size / size

        (0 until n).forEach { i ->
            layers.add(image.subList(i * size, (i + 1) * size))
        }
        return layers
    }

    fun solve2(input: String, width: Int, height: Int) {
        val image = input.map { it.toInt() - '0'.toInt() }.toList()
        val layers = buildLayers(width, height, image)
        val finalImage = mergeLayers(layers)
        finalImage.forEachIndexed { idx, pixel ->
            if(pixel == 1){
                print("#")
            }else{
                print(" ")
            }
            if ((idx + 1) % width == 0) {
                println()
            }
        }
    }

    fun mergeLayers(layers: List<List<Int>>): List<Int> {
        val size = layers.head.size
        return (0 until size).map { i ->
            var current = 2
            var layer = 0
            while (current == 2) {
                current = layers[layer][i]
                layer++
            }
            current
        }
    }

}