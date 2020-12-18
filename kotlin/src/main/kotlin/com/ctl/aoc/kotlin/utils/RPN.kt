package com.ctl.aoc.kotlin.utils

import java.util.*

interface RpnContext<T> {
    fun RpnOperator<T>.eval(left: T, right: T): T
    fun Char.parseOperand(): T
}

interface RpnOperatorContext {
    fun RpnOperator<*>.priority(): Int
}

sealed class ParsableRpnToken<T> {
    companion object {
        fun <T> parse(c: Char, rpnContext: RpnContext<T>): ParsableRpnToken<T> {
            return when {
                c.isDigit() -> rpnContext.run { RpnOperand(c.parseOperand()) }
                c == '+' -> RpnOperator.Plus()
                c == '-' -> RpnOperator.Minus()
                c == '*' -> RpnOperator.Mult()
                c == '/' -> RpnOperator.Div()
                c == '(' -> LeftParenthesis()
                c == ')' -> RightParenthesis()
                c == ' ' -> Whitespace()
                else -> throw Error("Unknown character: $c")
            }
        }
    }
}

class LeftParenthesis<T> : ParsableRpnToken<T>()
class RightParenthesis<T> : ParsableRpnToken<T>()
class Whitespace<T> : ParsableRpnToken<T>()

sealed class RpnToken<T> : ParsableRpnToken<T>() {
    fun asString(): String {
        return when (this) {
            is RpnOperand -> this.value.toString()
            is RpnOperator.Plus -> "+"
            is RpnOperator.Minus -> "-"
            is RpnOperator.Mult -> "*"
            is RpnOperator.Div -> "/"
        }
    }
}

data class RpnOperand<T>(val value: T) : RpnToken<T>()
sealed class RpnOperator<T> : RpnToken<T>() {
    class Plus<T> : RpnOperator<T>()
    class Minus<T> : RpnOperator<T>()
    class Mult<T> : RpnOperator<T>()
    class Div<T> : RpnOperator<T>()
}


private data class ShuntingYardState<T>(
        val output: Deque<RpnToken<T>> = ArrayDeque(),
        val operators: Deque<ParsableRpnToken<T>> = ArrayDeque()
) {
    fun addOperator(operator: RpnOperator<T>, operatorContext: RpnOperatorContext) {
        while (shouldPopOperator(operator, operatorContext)) {
            moveHeadOperatorToOutput()
        }
        operators.push(operator)
    }

    fun addRightParenthesis() {
        while (operators.isNotEmpty() && operators.peek() !is LeftParenthesis) {
            moveHeadOperatorToOutput()
        }
        if (operators.isEmpty()) {
            throw Error("Could not match closing (")
        }
        operators.pop()
    }

    fun moveAllToOutput() {
        while (operators.isNotEmpty()) {
            moveHeadOperatorToOutput()
        }
    }

    private fun moveHeadOperatorToOutput() {
        when (val head = operators.pop()) {
            is RpnToken -> {
                output.add(head)
            }
            else -> throw Error("Could not add $head to output")
        }
    }

    private fun shouldPopOperator(newOperator: RpnOperator<T>, operatorContext: RpnOperatorContext): Boolean = operatorContext.run {
        if (operators.isEmpty()) {
            return false
        }
        return when (val headOperator = operators.peek()) {
            is LeftParenthesis -> false
            is RpnOperator -> headOperator.priority() >= newOperator.priority()
            else -> true
        }
    }
}

data class RPN<T>(val tokens: List<RpnToken<T>>, val rpnContext: RpnContext<T>) {

    fun eval(): T {
        val output = ArrayDeque<T>()
        tokens.forEach { token ->
            when (token) {
                is RpnOperand -> output.push(token.value)
                is RpnOperator -> {
                    val right = output.pop()
                    val left = output.pop()
                    rpnContext.run {
                        output.push(token.eval(left, right))
                    }
                }
            }
        }
        assert(output.size == 1)
        return output.pop()
    }

    val value: T by lazy {
        eval()
    }

    companion object {
        fun <T> parse(input: String, rpnContext: RpnContext<T>, operatorContext: RpnOperatorContext = defaultRpnOperatorContext): RPN<T> {
            val finalState = input.map { ParsableRpnToken.parse(it, rpnContext) }
                    .fold(ShuntingYardState<T>()) { state, token ->
                        when (token) {
                            is RpnOperand -> state.output.add(token)
                            is RpnOperator -> state.addOperator(token, operatorContext)
                            is LeftParenthesis -> state.operators.push(token)
                            is RightParenthesis -> state.addRightParenthesis()
                            is Whitespace -> {
                            }
                        }
                        state
                    }
            finalState.moveAllToOutput()
            return RPN(finalState.output.toList(), rpnContext)
        }
    }

    override fun toString(): String {
        return tokens.joinToString(" ") { it.asString() }
    }
}

val longContext = object : RpnContext<Long> {
    override fun RpnOperator<Long>.eval(left: Long, right: Long): Long {
        return when (this) {
            is RpnOperator.Plus -> left + right
            is RpnOperator.Minus -> left - right
            is RpnOperator.Mult -> left * right
            is RpnOperator.Div -> left / right
        }
    }

    override fun Char.parseOperand(): Long = this.toString().toLong()
}

val defaultRpnOperatorContext = object : RpnOperatorContext {
    override fun RpnOperator<*>.priority(): Int = when (this) {
        is RpnOperator.Plus -> 1
        is RpnOperator.Minus -> 1
        is RpnOperator.Mult -> 2
        is RpnOperator.Div -> 2
    }
}