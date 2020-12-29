package com.ctl.aoc.kotlin.utils.grammar

import org.junit.jupiter.api.Test

internal class EarleyItemTest {
    private val simpleGrammar = Grammar(
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

    private val grammar = grammar {
        "Sum" `=` { !"Sum"; chars('+', '-'); !"Product" }
        "Sum" `=` "Product"
        "Product" `=` { !"Product"; chars('*', '/'); !"Factor" }
        "Product" `=` "Factor"
        "Factor" `=` { +'('; !"Sum"; +')' }
        "Factor" `=` "Number"
        "Number" `=` { +('0'..'9'); !"Number" }
        "Number" `=` { +('0'..'9') }
    }.build()

    val cesar: Int.(Int) -> Int = { other -> plus(other) }

    @Test
    internal fun cesar() {
        println(123.cesar(5))
    }

    @Test
    internal fun verifyTest() {
        simpleGrammar.run {
            println(earleyMatch("1+(2*3+4)"))
            println(earleyMatch("1+(2%3+4)"))
        }
        println("1+(2*3+4)".matches(grammar))
        println("1+(2%3+4)".matches(grammar))
    }
}