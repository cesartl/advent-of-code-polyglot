package com.ctl.aoc.kotlin.utils.grammar

data class Grammar(val startRuleName: String, val rules: List<GrammarRule>) {
    val rulesByName = rules.groupBy { it.name }
}

data class GrammarRule(val name: String, val elements: List<GrammarElement>)

sealed class GrammarElement
data class RuleRef(val ruleName: String) : GrammarElement()
sealed class TerminalElement : GrammarElement()
data class SingleChar(val char: Char) : TerminalElement()
data class Range(val charRange: CharRange) : TerminalElement()
data class CharClass(val allowedChars: Set<Char>) : TerminalElement()

class RuleBuilder {
    val elements = mutableListOf<GrammarElement>()

    fun ref(name: String) {
        elements.add(RuleRef(name))
    }

    operator fun String.not() {
        ref(this)
    }

    fun char(char: Char) {
        elements.add(SingleChar(char))
    }

    fun chars(vararg chars: Char) {
        elements.add(CharClass(chars.toSet()))
    }

    operator fun Char.unaryPlus(){
        elements.add(SingleChar(this))
    }

    operator fun CharRange.unaryPlus(){
        elements.add(Range(this))
    }
}

class GrammarBuilder {
    private val rules = mutableListOf<GrammarRule>()
    infix fun String.`=`(init: RuleBuilder.() -> Unit) {
        val ruleBuilder = RuleBuilder()
        ruleBuilder.init()
        rules.add(GrammarRule(this, ruleBuilder.elements))
    }

    infix fun String.`=`(rule: String) {
        rules.add(GrammarRule(this, listOf(RuleRef(rule))))
    }

    fun build(): Grammar {
        return Grammar(rules.first().name, rules)
    }
}

fun grammar(init: GrammarBuilder.() -> Unit): GrammarBuilder {
    val builder = GrammarBuilder()
    builder.init()
    return builder
}