package com.ctl.aoc.kotlin.utils.grammar


data class EarleyItem(val rule: GrammarRule, val startIdx: Int, val nextIdx: Int) {
    val nextElement: GrammarElement? by lazy {
        if (nextIdx >= rule.elements.size) {
            null
        } else {
            rule.elements[nextIdx]
        }
    }
}

class StateSet : Iterable<EarleyItem> {
    private val items: MutableList<EarleyItem> = mutableListOf()
    val size: Int
        get() = items.size

    fun add(item: EarleyItem) {
        if (!items.contains(item)) {
            items.add(item)
        }
    }

    operator fun get(idx: Int): EarleyItem {
        return items[idx]
    }

    override fun iterator(): Iterator<EarleyItem> = items.iterator()
}

class EarleyParser(private val grammar: Grammar) {
    private val stateSets = mutableMapOf<Int, StateSet>()

    public val earlyItems
        get() = stateSets.toMap()

    fun matches(input: String){
        TODO()
    }

    fun buildEarleyItems(input: String) {
        grammar.rules.filter { it.name == grammar.startRuleName }.forEach { rule ->
            val item = EarleyItem(rule, 0, 0)
            stateSets.computeIfAbsent(0) { StateSet() }.add(item)
        }

        var i = 0
        while (i < stateSets.size) {
            val currentState = stateSets.getValue(i)
            var j = 0
            while (j < currentState.size) {
                val currentItem = currentState[j]
                println("Current item $currentItem")
                when (val element = currentItem.nextElement) {
                    is RuleRef -> predict(i, currentState, element)
                    is TerminalElement -> scan(i, currentItem, element, input)
                    null -> complete(i, currentItem)
                }
                j++
            }
            i++
            println("i: $i, sets: ${stateSets.size}")
        }
    }

    private fun predict(i: Int, stateSet: StateSet, ruleRef: RuleRef) {
        grammar.rulesByName.getValue(ruleRef.ruleName).forEach { rule ->
            stateSet.add(EarleyItem(rule, i, 0))
        }
    }

    private fun scan(i: Int, item: EarleyItem, terminalElement: TerminalElement, input: String) {
        if (i < input.length && input[i].isMatched(terminalElement)) {
            val newItem = item.copy(nextIdx = item.nextIdx + 1)
            println("Adding $newItem to ${i+1}")
            stateSets.computeIfAbsent(i + 1) { StateSet() }.add(newItem)
        }
    }

    private fun complete(i: Int, item: EarleyItem) {
        stateSets.getValue(item.startIdx).forEach { oldItem ->
            when (val element = oldItem.nextElement) {
                is RuleRef -> {
                    if (element.ruleName == item.rule.name) {
                        val newItem = oldItem.copy(nextIdx = oldItem.nextIdx + 1)
                        stateSets.getValue(i).add(newItem)
                    }
                }
            }
        }
    }

    private fun Char.isMatched(element: TerminalElement): Boolean {
        return when (element) {
            is SingleChar -> element.char == this
            is Range -> element.charRange.contains(this)
            is CharClass -> element.allowedChars.contains(this)
        }
    }
}