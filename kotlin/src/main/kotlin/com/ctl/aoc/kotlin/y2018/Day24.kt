package com.ctl.aoc.kotlin.y2018

import java.util.regex.Pattern

object Day24 {
    val groupPattern = Pattern.compile("([\\d]+) units each with ([\\d]+) hit points (\\([\\w ,;]+\\) )?with an attack that does ([\\d]+) ([\\w ]+) damage at initiative ([\\d]+)")

    enum class Team(val teamName: String) { IMMUNE_SYSTEM("Immune System"), INFECTION("Infection") }

    fun parseTeam(teamName: String): Team = Team.values().find { it.teamName == teamName }
            ?: throw IllegalArgumentException(teamName)

    data class FightingGroup(val id: Int, val size: Int, val hp: Int, val attackPower: Int, val attackType: String, val initiative: Int, val weakTo: Set<String>, val immuneTo: Set<String>, val team: Team) {
        val effectivePower = size * attackPower

        fun damageTo(other: FightingGroup): Int {
            return when {
                other.immuneTo.contains(this.attackType) -> 0
                other.weakTo.contains(this.attackType) -> 2 * effectivePower
                else -> effectivePower
            }
        }

        fun receiveDamage(from: FightingGroup): FightingGroup {
            val dmg = from.damageTo(this)
            val loss = dmg / hp
            return this.copy(size = size - loss)
        }
    }

    fun parse(s: String, team: Team, id: Int): FightingGroup {
        val m = groupPattern.matcher(s)
        if (m.matches()) {
            val size = m.group(1).toInt()
            val hp = m.group(2).toInt()
            val attackPower = m.group(4).toInt()
            val attackType = m.group(5)
            val initiative = m.group(6).toInt()

            val weakTo = mutableSetOf<String>()
            val immuneTo = mutableSetOf<String>()
            if (m.group(3) != null) {
                val meta = m.group(3).trim().drop(1).dropLast(1)
                meta.split(";").map { it.trim() }.forEach { d ->
                    val foo = d.split("to").map { it.trim() }
                    val toAdd = foo[1].split(",").map { it.trim() }
                    when {
                        foo[0] == "immune" -> immuneTo.addAll(toAdd)
                        foo[0] == "weak" -> weakTo.addAll(toAdd)
                        else -> throw IllegalArgumentException(foo[0])
                    }
                }
            }
            return FightingGroup(id = id, size = size, hp = hp, attackPower = attackPower, attackType = attackType, initiative = initiative, weakTo = weakTo, immuneTo = immuneTo, team = team)
        }
        throw IllegalArgumentException(s)
    }

    data class Targetting(val groupUsed: Set<Int>, val targets: List<Pair<Int, Int>>)

    data class Battlefield(val groups: List<FightingGroup>) {
        fun battleRound(): Battlefield {
            println("new round")
            val groupById = groups.map { it.id to it }.toMap().toMutableMap()

            val targettingOrder = groups.sortedWith(compareBy({ -it.effectivePower }, { -1 * it.initiative }))

            val groupSequence = groups.asSequence()

            val battlePlan = targettingOrder.fold(Targetting(setOf(), listOf())) { acc, fightingGroup ->
                val target = groupSequence.filter { !acc.groupUsed.contains(it.id) }.filter { it.team != fightingGroup.team }.sortedWith(compareBy({ -fightingGroup.damageTo(it) }, { -it.effectivePower }, { -it.initiative })).firstOrNull()
                if (target != null) {
                    Targetting(acc.groupUsed + target.id, acc.targets + (fightingGroup.id to target.id))
                } else {
                    acc
                }
            }

            battlePlan.targets.forEach { (attackingId, defendingId) ->
                val attacking = groupById[attackingId]
                if (attacking != null) {
                    var defending = groupById[defendingId]!!
                    defending = defending.receiveDamage(attacking)
                    if (defending.size <= 0) {
                        groupById.remove(defendingId)
                    } else {
                        groupById[defendingId] = defending
                    }
                }
            }

            return this.copy(groups = groupById.values.toList())
        }
    }

    fun parseInput(lines: Sequence<String>): Battlefield {
        var id = 0
        val team1Name1 = parseTeam(lines.first().trim().dropLast(1))
        val team1 = lines.drop(1).takeWhile { it != "" }.map { parse(it, team1Name1, id++) }.toList()


        val foo = lines.dropWhile { it != "" }
        val teamName2 = parseTeam(foo.drop(1).first().trim().dropLast(1))
        val team2 = foo.drop(2).map { parse(it, teamName2, id++) }.toList()
        return Battlefield(team1 + team2)
    }

    fun solve1(lines: Sequence<String>): Int {
        var battlefield = parseInput(lines)
        while (battlefield.groups.groupBy { it.team }.size > 1) {
            battlefield = battlefield.battleRound()
        }
        return battlefield.groups.map { it.size }.sum()
    }
}