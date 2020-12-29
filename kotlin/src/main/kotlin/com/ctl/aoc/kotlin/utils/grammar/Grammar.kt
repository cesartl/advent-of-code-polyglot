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