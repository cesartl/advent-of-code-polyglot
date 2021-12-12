package com.ctl.aoc.kotlin.y2015

import kotlin.math.max

object Day21 {

    data class Item(val name: String, val cost: Int, val damage: Int, val armor: Int)

    data class Character(val damage: Int, val armor: Int, val hp: Int) {
        fun isAlive() = hp > 0

        fun receiveAttack(attacker: Character): Character = this.copy(hp = this.hp - max(1, attacker.damage - this.armor))
    }

    fun doCombat(p1: Character, p2: Character): Pair<Character, Character> {
        var c1 = p1
        var c2 = p2
        while (c1.isAlive() && c2.isAlive()) {
            c2 = c2.receiveAttack(c1)
            if (!c2.isAlive()) {
                break
            }
            c1 = c1.receiveAttack(c2)
        }
        return c1 to c2
    }

    fun playerWins(player: Character, boss: Character): Boolean {
        val (endPlayer, endBoss) = doCombat(player, boss)
//        println("player $endPlayer")
//        println("boss $endBoss")
        return endPlayer.isAlive()
    }

    data class GameConfiguration(val items: List<Item>) {
        val cost: Int = items.map { it.cost }.sum()
        fun playerWins(playerHp: Int, boss: Character): Boolean {
//            println("Trying with $items")
            var damage = 0
            var armor = 0
            items.forEach {
                damage += it.damage
                armor += it.armor
            }
            val player = Character(damage, armor, playerHp)
            return Day21.playerWins(player, boss)
        }
    }

    val weapons = listOf(
            Item("Dagger", 8, 4, 0),
            Item("Shortsword", 10, 5, 0),
            Item("Warhammer", 25, 6, 0),
            Item("Longsword", 40, 7, 0),
            Item("Greataxe", 74, 8, 0)
    )

    val armors = listOf(
            Item("Leather", 13, 0, 1),
            Item("Chainmail", 31, 0, 2),
            Item("splintmail", 53, 0, 3),
            Item("bandemail", 75, 0, 4),
            Item("Platemail", 102, 0, 5)
    )

    val rings = listOf(
            Item("Damage +1", 25, 1, 0),
            Item("Damage +2", 50, 2, 0),
            Item("Damage +3", 100, 3, 0),
            Item("Defense +1", 20, 0, 1),
            Item("Defense +2", 40, 0, 2),
            Item("Defense +3", 80, 0, 3)
    ).sortedBy { it.cost }

    fun allGameConfigurations(): Sequence<GameConfiguration> = sequence {
        weapons.forEach { weapon ->
            (-1 until armors.size).forEach { armorIdx ->
                (-1 until rings.size).forEach { leftRing ->
                    (leftRing until rings.size).forEach { rightRing ->
                        val items = mutableListOf(weapon)
                        if (armorIdx >= 0) {
                            items.add(armors[armorIdx])
                        }
                        if (leftRing >= 0) {
                            items.add(rings[leftRing])
                        }
                        if (rightRing > leftRing) {
                            items.add(rings[rightRing])
                        }
                        yield(GameConfiguration(items))
                    }
                }

            }
        }
    }

    fun solve1(boss: Character): Int {
        val config = allGameConfigurations().filter { it.playerWins(100, boss) }.minByOrNull { it.cost }!!
        println("Config $config")
        return config.cost
    }

    fun solve2(boss: Character): Int {
        val config = allGameConfigurations().filter { !it.playerWins(100, boss) }.maxByOrNull { it.cost }!!
        println("Config $config")
        return config.cost
    }

}