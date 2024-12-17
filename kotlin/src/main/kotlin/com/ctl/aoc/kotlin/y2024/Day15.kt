package com.ctl.aoc.kotlin.y2024

import com.ctl.aoc.kotlin.utils.*

object Day15 {

    data class Warehouse(
        val walls: Set<Position>,
        val boxes: Set<Position>,
        val robot: Position,
        val xRange: IntRange,
        val yRange: IntRange
    ) {

        val gpsValue: Int by lazy {
            boxes.sumOf { (x, y) -> x + 100 * y }
        }

        fun moveRobot(instruction: Char): Warehouse {
            val direction = parseInstruction(instruction)
            val nextRobot = direction.move(robot)

            if (walls.contains(nextRobot)) {
                return this
            }

            if (!boxes.contains(nextRobot)) {
                return this.copy(robot = nextRobot)
            }

            val boxesToMove = generateSequence(nextRobot) { direction.move(it) }
                .takeWhile { boxes.contains(it) }
                .toList()

            val emptySlot = direction.move(boxesToMove.last())

            if (walls.contains(emptySlot)) {
                return this
            }

            val newBoxes = boxes.toMutableSet()
            newBoxes.removeAll(boxesToMove.toSet())
            newBoxes.addAll(boxesToMove.map { direction.move(it) })
            return this.copy(boxes = newBoxes, robot = nextRobot)
        }

        fun print() {
            yRange.forEach { y ->
                xRange.forEach { x ->
                    val p = Position(x, y)
                    when {
                        walls.contains(p) -> print('#')
                        boxes.contains(p) -> print('O')
                        robot == p -> print('@')
                        else -> print('.')
                    }
                }
                println()
            }
        }
    }

    fun solve1(input: String): Int {
        val (warehouse, instructions) = parseWarehouse(input)

        val result = instructions.fold(warehouse) { w, i ->
            w.moveRobot(i)
        }

        return result.gpsValue
    }

    fun solve2(input: String): Int {
        val (largeWarehouse, instructions) = parseLargeWarehouse(input)
        largeWarehouse.print()
        TODO()
    }

    data class LargeBox(val left: Position, val right: Position)

    data class LargeWarehouse(
        val walls: Set<Position>,
        val boxes: Set<LargeBox>,
        val robot: Position,
        val xRange: IntRange,
        val yRange: IntRange
    ) {

        private val boxPositions = boxes.flatMap { listOf(it.left, it.right) }.toSet()

        fun moveRobot(instruction: Char): LargeWarehouse {
            TODO()
        }

        fun print() {
            yRange.forEach { y ->
                var x = 0
                while(x in xRange){
                    val p = Position(x, y)
                    when {
                        walls.contains(p) -> print('#')
                        boxPositions.contains(p) -> {
                            print("[]")
                            x++
                        }

                        robot == p -> print('@')
                        else -> print('.')
                    }
                    x++
                }
                println()
            }
        }
    }

    private fun parseWarehouse(input: String): Pair<Warehouse, String> {
        val (gridString, instructions) = input.split("\n\n")
        val grid = parseGrid(gridString.trim().lineSequence())

        val walls = grid.map.asSequence()
            .filter { it.value == '#' }
            .map { it.key }
            .toSet()

        val boxes = grid.map.asSequence()
            .filter { it.value == 'O' }
            .map { it.key }
            .toSet()

        val robot = grid.map.asSequence()
            .filter { it.value == '@' }
            .map { it.key }
            .single()

        return Warehouse(walls, boxes, robot, grid.xRange, grid.yRange) to instructions.trim().replace("\n", "")
    }

    private fun parseLargeWarehouse(input: String): Pair<LargeWarehouse, String> {
        val (gridString, instructions) = input.split("\n\n")
        val grid = parseGrid(gridString.trim().lineSequence().map { it.enlarge() })

        val walls = grid.map.asSequence()
            .filter { it.value == '#' }
            .map { it.key }
            .toSet()

        val boxes = grid.map.asSequence()
            .filter { it.value == '[' }
            .map { it.key }
            .map { LargeBox(it, it.copy(x = it.x + 1)) }
            .toSet()

        val robot = grid.map.asSequence()
            .filter { it.value == '@' }
            .map { it.key }
            .single()


        return LargeWarehouse(walls, boxes, robot, grid.xRange, grid.yRange) to instructions.trim().replace("\n", "")
    }

}

private fun String.enlarge(): String {
    return this.replace("#", "##")
        .replace("O", "[]")
        .replace(".", "..")
        .replace("@", "@.")
}

private fun parseInstruction(instruction: Char): Orientation {
    return when (instruction) {
        '^' -> N
        'v' -> S
        '<' -> W
        '>' -> E
        else -> throw IllegalArgumentException("Invalid instruction '$instruction'")
    }
}
