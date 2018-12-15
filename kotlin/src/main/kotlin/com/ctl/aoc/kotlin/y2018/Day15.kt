package com.ctl.aoc.kotlin.y2018

import com.ctl.aoc.kotlin.utils.Dijkstra
import com.ctl.aoc.kotlin.utils.MutableTable
import com.ctl.aoc.kotlin.utils.NodeGenerator
import com.ctl.aoc.kotlin.utils.findPath
import java.lang.IllegalArgumentException
import java.lang.StringBuilder

sealed class BattleElement {
    abstract fun print(): String
    override fun toString(): String = this.javaClass.simpleName
}

object Wall : BattleElement() {
    override fun print(): String = "#"
}

sealed class NonWall : BattleElement()

object Empty : NonWall() {
    override fun print(): String = "."

}

sealed class BattleUnit : NonWall() {
    abstract val power: Int
    abstract val hp: Int
    abstract val id: Int
    abstract fun isEnemyWith(battleUnit: BattleUnit): Boolean

    abstract fun receiveAttack(enemy: BattleUnit): BattleUnit?
}

data class Elf(override val power: Int, override val hp: Int, override val id: Int) : BattleUnit() {
    override fun receiveAttack(enemy: BattleUnit): BattleUnit? {
        val afterDamage = this.copy(hp = hp - enemy.power)
        return if (afterDamage.hp > 0) afterDamage else null
    }

    override fun isEnemyWith(battleUnit: BattleUnit): Boolean {
        return when (battleUnit) {
            is Goblin -> true
            else -> false
        }
    }

    override fun print(): String = "E"
}

data class Goblin(override val power: Int, override val hp: Int, override val id: Int) : BattleUnit() {
    override fun receiveAttack(enemy: BattleUnit): BattleUnit? {
        val afterDamage = this.copy(hp = hp - enemy.power)
        return if (afterDamage.hp > 0) afterDamage else null
    }

    override fun isEnemyWith(battleUnit: BattleUnit): Boolean {
        return when (battleUnit) {
            is Elf -> true
            else -> false
        }
    }

    override fun print(): String = "G"
}

data class Pathing(val nonWall: NonWall, val x: Int, val y: Int)

data class Battleground(val table: MutableTable<BattleElement>) {

    private val baseDistance = 100000

    private val comparator = compareBy<Map.Entry<Pathing, Long>>({ it.value / baseDistance }, { it.key.y }, { it.key.x })
    private val battleComparator = compareBy<Map.Entry<Pathing, Long>>({ it.value / baseDistance }, { (it.key.nonWall as BattleUnit).hp }, { it.key.y }, { it.key.x })


    var dead = mutableSetOf<Int>()

    fun clone(f: (BattleElement) -> BattleElement): Battleground {
        val newTable = MutableTable.mutableTable<BattleElement>()
        table.map.forEach { y, row -> row.forEach { x, e -> newTable.put(x, y, f(e)) } }
        return this.copy(table = newTable)
    }

    fun isBattleOver(): Boolean = teams().size == 1


    fun doRound(): Boolean {
        val roundOrder = table.map.entries.flatMap { (y, row) -> row.entries.filter { it.value is NonWall }.map { (x, e) -> Pathing(e as NonWall, x, y) } }
//        println(roundOrder.filter { it.nonWall is BattleUnit })
        roundOrder.forEach { e ->
            if (e.nonWall is BattleUnit) {
                val unit = e.nonWall
                if (!dead.contains(unit.id)) {
                    if (doUnitRound(e)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    fun teams() = table.all().filter { it is BattleUnit }.groupBy { it.javaClass.kotlin }

    private fun doUnitRound(currentUnit: Pathing, attackOnly: Boolean = false): Boolean {
        val pathingResult = Dijkstra.traverse(currentUnit, null, { generateNeighbours(currentUnit, it) }, { _, to -> distance(to) }, emptyList())


        val enemies = pathingResult.steps.filter { it.key.nonWall is BattleUnit }.filter { (it.key.nonWall as BattleUnit).isEnemyWith(currentUnit.nonWall as BattleUnit) }


        val firstEnemy = enemies.entries.sortedWith(battleComparator).firstOrNull()

        if (firstEnemy != null) {
            if (firstEnemy.value < 2 * baseDistance) {
//                println("doing attack $currentUnit -> ${firstEnemy.key}")
                val e = (firstEnemy.key.nonWall as BattleUnit).receiveAttack(currentUnit.nonWall as BattleUnit)
                if (e != null) {
                    table.put(firstEnemy.key.x, firstEnemy.key.y, e)
                } else {
//                    println("Death of ${firstEnemy.key}")
                    dead.add((firstEnemy.key.nonWall as BattleUnit).id)
                    table.put(firstEnemy.key.x, firstEnemy.key.y, Empty)
                }
            } else if (!attackOnly) {
                //doing a move
                // need to find all the ranges
                val targets = enemies.entries.flatMap { entry -> pathingAround(entry.key).filter { it.nonWall is Empty }.map { it to pathingResult.steps[it] }.filter { it.second != null }.map { it.first to it.second!! }.toList() }.toMap().entries.sortedWith(comparator)
                val path = pathingResult.findPath(targets.first().key)
                val newPosition = path[1]
                table.put(currentUnit.x, currentUnit.y, Empty)
                table.put(newPosition.x, newPosition.y, currentUnit.nonWall)
                doUnitRound(Pathing(currentUnit.nonWall, newPosition.x, newPosition.y), true)
            }
        } else if (table.all().filterIsInstance(BattleUnit::class.java).filter { it.isEnemyWith(currentUnit.nonWall as BattleUnit) }.isEmpty()) {
            return true
        }
        return false
    }

    private fun distance(to: Pathing): Long = to.y * 33L + to.x + baseDistance

    private fun generateNeighbours(start: Pathing, pathing: Pathing): Sequence<Pathing> {
        // if we reach a battle unit there is nothing else to go
        if (pathing.nonWall is BattleUnit && pathing != start) {
            return emptySequence()
        }

        //otherwise we can try to go up, down, left, right
        return pathingAround(pathing)
    }

    private fun pathingAround(pathing: Pathing): Sequence<Pathing> {
        return sequenceOf(
                pathing.x - 1 to pathing.y,
                pathing.x to pathing.y - 1,
                pathing.x + 1 to pathing.y,
                pathing.x to pathing.y + 1
        ).map { (it.first to it.second) to table.get(it.first, it.second)!! }
                .filter {
                    when (it.second) {
                        is Wall -> false
                        else -> true
                    }
                }
                .map { Pathing((it.second) as NonWall, it.first.first, it.first.second) }
    }

    fun print(): String {
        val builder = StringBuilder()
        table.map.entries.sortedBy { it.key }.forEach { (y, row) ->
            row.entries.sortedBy { it.key }.forEach { (x, e) -> builder.append(e.print()) }
            builder.append("\n")
        }
        return builder.toString()
    }
}

object Day15 {

    fun parse(lines: List<String>): Battleground {
        val table = MutableTable.mutableTable<BattleElement>()
        for (y in lines.indices) {
            val row = lines[y]
            for (x in row.indices) {
                val e = when (row[x]) {
                    '#' -> Wall
                    'G' -> Goblin(power = 3, hp = 200, id = y * 33 + x)
                    'E' -> Elf(power = 3, hp = 200, id = y * 33 + x)
                    '.' -> Empty
                    else -> throw IllegalArgumentException("${row[x]}")
                }
                table.put(x, y, e)
            }
        }
        return Battleground(table)
    }

    fun solve1(lines: List<String>): Long {
        var b = parse(lines)
        println()
        println("solve1")
        println(b.print())
        return battleOutcome(b)
    }

    fun battleOutcome(b: Battleground): Long {
        var n = 0L

        while (true) {
            var finishEarly = b.doRound()
            if (finishEarly) {
                println("finish early")
                break
            }
            n += 1
        }
        val remainers = b.table.all().filterIsInstance(BattleUnit::class.java)
        val totalHp = remainers.map { it.hp }.sum()
//        println("remainers $remainers")
        println("totalHp $totalHp")
        println("n: $n")
        println("remaining: ${remainers.size}")
//        println(b.print())
        return (n) * totalHp
    }

    fun solve2(lines: List<String>): Long {
        println()
        println("solve2")
        val orginal = parse(lines)
        val elfs = orginal.table.all().filterIsInstance(Elf::class.java).count()
        var powerOffset = 1
        while (true) {
            println("power offset: $powerOffset")
            val b = orginal.clone {
                when (it) {
                    is Elf -> it.copy(power = it.power + powerOffset)
                    else -> it
                }
            }
            val outcome = battleOutcome(b)
            val remainingElf = b.table.all().filterIsInstance(Elf::class.java).count()
            if (remainingElf == elfs) {
                println(b.print())
                return outcome
            }
            powerOffset += 1
        }
    }
}