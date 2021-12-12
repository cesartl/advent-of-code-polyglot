package com.ctl.aoc.kotlin.y2015

import com.ctl.aoc.kotlin.y2015.Day22.Spell.ActionSpell.Drain
import com.ctl.aoc.kotlin.y2015.Day22.Spell.ActionSpell.MagicMissile
import com.ctl.aoc.kotlin.y2015.Day22.Spell.EffectSpell.*
import java.util.*
import kotlin.math.max

object Day22 {
    data class Character(val damage: Int = 0, val armor: Int = 0, val hp: Int, val mana: Int = 500, val temporaryArmor: Int = 0) {
        fun isAlive() = hp > 0
        fun adjustHp(hpModifier: Int): Character = this.copy(hp = this.hp + hpModifier)
        fun adjustMana(manaModifier: Int): Character = this.copy(mana = this.mana + manaModifier)
        fun receivePhysicalAttack(attacker: Character): Character = this.copy(hp = this.hp - max(1, attacker.damage - this.armor - this.temporaryArmor), temporaryArmor = 0)

        companion object {
            fun player(hp: Int, mana: Int): Character = Character(hp = hp, mana = mana)
            fun boss(hp: Int, damage: Int): Character = Character(hp = hp, damage = damage)
        }
    }

    sealed class Effect {
        abstract fun affectPlayer(player: Character): Character
        abstract fun affectBoss(boss: Character): Character
        abstract fun reduceRemaining(): Effect?
        abstract fun name(): String
        abstract val turnRemaining: Int

        data class ShieldEffect(override val turnRemaining: Int = 5) : Effect() {
            override fun affectPlayer(player: Character): Character = player.copy(temporaryArmor = 7)
            override fun affectBoss(boss: Character): Character = boss
            override fun name(): String = "Shield"
            override fun reduceRemaining(): Effect? {
                return if (this.turnRemaining <= 1) null else this.copy(turnRemaining = this.turnRemaining - 1)
            }
        }

        data class PoisonEffect(override val turnRemaining: Int = 6) : Effect() {
            override fun affectPlayer(player: Character): Character = player
            override fun affectBoss(boss: Character): Character = boss.adjustHp(-3)
            override fun name(): String = "Poison"
            override fun reduceRemaining(): Effect? {
                return if (this.turnRemaining <= 1) null else this.copy(turnRemaining = this.turnRemaining - 1)
            }
        }

        data class RechargeEffect(override val turnRemaining: Int = 5) : Effect() {
            override fun affectPlayer(player: Character): Character = player.adjustMana(101)
            override fun affectBoss(boss: Character): Character = boss
            override fun name(): String = "Recharge"
            override fun reduceRemaining(): Effect? {
                return if (this.turnRemaining <= 1) null else this.copy(turnRemaining = this.turnRemaining - 1)
            }
        }
    }

    sealed class Spell {
        abstract val manaCost: Int

        sealed class ActionSpell : Spell() {
            abstract val damage: Int
            abstract val heal: Int

            object MagicMissile : ActionSpell() {
                override val manaCost: Int = 53
                override val damage: Int = 4
                override val heal: Int = 0
            }

            object Drain : ActionSpell() {
                override val manaCost: Int = 73
                override val damage: Int = 2
                override val heal: Int = 2
            }
        }

        sealed class EffectSpell : Spell() {
            abstract fun effect(): Effect

            object Shield : EffectSpell() {
                override val manaCost: Int = 113
                override fun effect(): Effect = Effect.ShieldEffect()
            }

            object Poison : EffectSpell() {
                override val manaCost: Int = 173
                override fun effect(): Effect = Effect.PoisonEffect()
            }

            object Recharge : EffectSpell() {
                override val manaCost: Int = 229
                override fun effect(): Effect = Effect.RechargeEffect()
            }
        }
    }

    data class GameState(val player: Character, val boss: Character, val effects: List<Effect> = listOf(), val totalMana: Int = 0, val spells: List<Spell> = listOf(), val previous: GameState? = null, val hardMode: Boolean = false) {

        private fun applyEffects(): GameState {
            val (newPlayer, newBoss) = effects.fold(player to boss) { (p, b), effect ->
                //                println("Applying ${effect.name()}")
                (effect.affectPlayer(p) to effect.affectBoss(b))
            }
            val newEffects = effects.mapNotNull { it.reduceRemaining() }
            return GameState(newPlayer, newBoss, newEffects, previous = this)
        }

        fun isFinished(): Boolean = !(player.isAlive() && boss.isAlive())

        fun hasRecharge(): Boolean = effects.map { it.name() }.contains("Recharge")

        fun canPlayerCastSpell(spell: Spell): Boolean {
//            val manaAvailable = player.mana + (if (hasRecharge()) 101 else 0)
            val manaAvailable = player.mana
            if (manaAvailable < spell.manaCost) return false
            return when (spell) {
                is Spell.EffectSpell -> effects.find { it.name() == spell.effect().name() }?.let { it.turnRemaining <= 1 }
                        ?: true
                else -> true
            }
        }

        fun nextState(spell: Spell): GameState {
//            println("-- Player's turn--")
//            println("Player: $player")
//            println("Boss has ${boss.hp} HP")
//            println("Effects ${this.effects}")
            //first we apply all the effects

            var afterEffect = applyEffects()
            var newPlayer = afterEffect.player
            var newBoss = afterEffect.boss
            val newEffects = afterEffect.effects.toMutableList()

            if (hardMode) {
                newPlayer = newPlayer.adjustHp(-1)
                if (!newPlayer.isAlive()) {
                    return this.copy(player = newPlayer, boss = newBoss, effects = newEffects)
                }
            }

            //player's turn
//            println("Player cast ${spell.javaClass.simpleName}")
            when (spell) {
                is Spell.ActionSpell -> {
                    newPlayer = newPlayer.adjustHp(spell.heal)
                    newBoss = newBoss.adjustHp(-spell.damage)
                }
                is Spell.EffectSpell -> {
                    newEffects.add(spell.effect())
                }
            }
            newPlayer = newPlayer.adjustMana(-spell.manaCost)
            if (newPlayer.mana < 0) throw IllegalArgumentException("Not enough mana")

            //apply effects before boss' turn
//            println("")
//            println("-- Boss Turn --")
//            println("Player: $newPlayer")
//            println("Boss has ${newBoss.hp} HP")

            afterEffect = afterEffect.copy(player = newPlayer, boss = newBoss, effects = newEffects, previous = afterEffect).applyEffects()
            newPlayer = afterEffect.player
            newBoss = afterEffect.boss

            if (newBoss.isAlive()) {
                //boss turn
                newPlayer = newPlayer.receivePhysicalAttack(newBoss)
            }
            return this.copy(player = newPlayer, boss = newBoss, effects = afterEffect.effects, totalMana = totalMana + spell.manaCost, spells = this.spells + spell, previous = afterEffect)
        }
    }

    private val spells = listOf(MagicMissile, Drain, Shield, Poison, Recharge)

    fun solve1(player: Character, boss: Character): Int? {
        val gameState = GameState(player, boss)
        return searchLeastMana(gameState)
    }

    fun solve2(player: Character, boss: Character): Int? {
        val gameState = GameState(player, boss, hardMode = true)
        return searchLeastMana(gameState)
    }

    private fun searchLeastMana(gameState: GameState): Int? {
        val queue: Deque<GameState> = ArrayDeque()
        queue.add(gameState)
        val winingStates = mutableListOf<GameState>()
        var bestWinning = Int.MAX_VALUE
        while (queue.isNotEmpty()) {
            val current = queue.removeLast()
    //            println("Doing $current")
            if (current.isFinished()) {
                if (current.player.isAlive()) {
                    winingStates.add(current)
                    bestWinning = bestWinning.coerceAtMost(current.totalMana)
                }
            } else {
                if (current.totalMana <= bestWinning) {
                    val availableSpells = spells.filter { current.canPlayerCastSpell(it) }
                    availableSpells.forEach { spell ->
                        //                    println("Casting ${spell.javaClass.simpleName}")
                        val next = current.nextState(spell)
                        if (next.totalMana <= bestWinning) {
                            queue.add(next)
                        }
                    }
                }
            }
        }
        println("Wining ${winingStates.size}")
        val leastMana = winingStates.minByOrNull { it.totalMana }
        println(leastMana?.spells?.map { it.javaClass.simpleName })
        println(leastMana?.spells?.map { it.manaCost }?.sum())
        return leastMana?.totalMana
    }
}