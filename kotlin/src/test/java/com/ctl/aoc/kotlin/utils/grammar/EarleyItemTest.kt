package com.ctl.aoc.kotlin.utils.grammar

import org.junit.jupiter.api.Test

internal class EarleyItemTest {
    val simpleGrammar = Grammar(
            startRuleName = "Sum",
            rules = listOf(
                    GrammarRule("Sum", listOf(RuleRef("Sum"), CharClass(setOf('+', '-')), RuleRef("Product"))),
                    GrammarRule("Sum", listOf(RuleRef("Product"))),
                    GrammarRule("Product", listOf(RuleRef("Product"), CharClass(setOf('*', '/')), RuleRef("Factor"))),
                    GrammarRule("Product", listOf(RuleRef("Factor"))),
                    GrammarRule("Factor", listOf(SingleChar('('), RuleRef("Sum"), SingleChar(')'))),
                    GrammarRule("Factor", listOf(RuleRef("Number"))),
                    GrammarRule("Number", listOf(Range('0'..'9'), RuleRef("Number"))),
                    GrammarRule("Number", listOf(Range('0'..'9')))
            )
    )

    @Test
    internal fun verifyTest() {
        simpleGrammar.run {
            println(earleyMatch("1+(2*3+4)"))
            println(earleyMatch("1+(2%3+4)"))
        }
        println("1+(2*3+4)".matches(simpleGrammar))
        println("1+(2%3+4)".matches(simpleGrammar))
    }
}