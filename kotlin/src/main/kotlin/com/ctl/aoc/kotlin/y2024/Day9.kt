package com.ctl.aoc.kotlin.y2024

object Day9 {
    fun solve1(input: String): Long {
        val files = input
            .trim()
            .map { it.digitToInt() }
        val size = files.sum()
        val blocks = IntArray(size) { -1 }

        var isFile = true
        var current = 0
        files.forEachIndexed { index, n ->
            if (isFile) {
                val id = index / 2
                repeat(n) {
                    blocks[current] = id
                    current++
                }
                isFile = false
            } else {
                current += n
                isFile = true
            }
        }

        var left = 0
        var right = size - 1

        while (left < right) {
            while (blocks[left] != -1) {
                left++
            }
            blocks[left] = blocks[right]
            blocks[right] = -1
            right--
            while (right >= 0 && blocks[right] == -1) {
                right--
            }
            left++
        }

        val last = blocks.indexOf(-1)
        return blocks.slice(0 until last)
            .foldIndexed(0L) { index, acc, i ->
                acc + index * i.toLong()
            }
    }

    data class FileSpec(val id: Int, val index: Int, val size: Int)

    class FreeSpace(var index: Int, var size: Int) {
        override fun toString(): String {
            return "$index -> ${index + size}"
        }
    }

    fun solve2(input: Sequence<Char>): Long {
        val files = input
            .mapNotNull { it.digitToIntOrNull() }
        val blocks = IntArray(100_000) { -1 }

        var isFile = true
        var current = 0
        val fileSpecs = mutableListOf<FileSpec>()
        val freeSpaces = mutableListOf<FreeSpace>()
        files.forEachIndexed { index, n ->
            if (isFile) {
                val id = index / 2
                fileSpecs.add(FileSpec(id, current, n))
                repeat(n) {
                    blocks[current] = id
                    current++
                }
                isFile = false
            } else {
                freeSpaces.add(FreeSpace(current, n))
                current += n
                isFile = true
            }
        }


        var checkSum = 0L
        //Key: Size, Value -> Min Index
        val sizeMap: MutableMap<Int, Int> = HashMap()
        repeat(10) {
            sizeMap[it] = 0
        }
        fileSpecs.reversed().forEach { (id, position, size) ->
            val minIndex = sizeMap[size]!!
//            println("MinIndex: $minIndex")
            val match = findEmptyBlock(freeSpaces, position, size, minIndex)
            if (match != null) {
                val (f, freeSpace) = match
                repeat(size) { i ->
                    checkSum += id * (freeSpace.index + i).toLong()
                }

                (size..9).forEach {
                    sizeMap[it] = f
                }

                freeSpace.index += size
                freeSpace.size -= size
                if (freeSpace.size == 0) {
                    freeSpaces.removeAt(f)
                }
            } else {
                repeat(size) { i ->
                    checkSum += id * (position + i).toLong()
                }
            }
        }
        return checkSum
    }

    private fun findEmptyBlock(
        freeSpaces: MutableList<FreeSpace>,
        position: Int,
        size: Int,
        minIndex: Int
    ): IndexedValue<FreeSpace>? {
        return (minIndex until freeSpaces.size).firstNotNullOfOrNull { i ->
            val freeSpace = freeSpaces[i]
            if (freeSpace.size >= size && freeSpace.index < position) {
                IndexedValue(i, freeSpace)
            } else {
                null
            }
        }
    }
}
