package com.ctl.aoc.kotlin.utils

import java.util.*

interface RpnContext<T> {
    fun RpnOperator<T>.eval(left: T, right: T): T
    fun String.parseLiteral(): T
}

interface RpnOperatorContext {
    fun RpnOperator<*>.priority(): Int
}

sealed class ParsableRpnToken<T> {
    companion object {
        fun <T> parse(c: Char, rpnContext: RpnContext<T>): ParsableRpnToken<T> {
            return when {
                c.isDigit() -> Digit(c)
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
class Digit<T>(val c: Char) : ParsableRpnToken<T>()

sealed class RpnToken<T> : ParsableRpnToken<T>() {
    fun asString(): String {
        return when (this) {
            is Literal -> this.value.toString()
            is RpnOperator.Plus -> "+"
            is RpnOperator.Minus -> "-"
            is RpnOperator.Mult -> "*"
            is RpnOperator.Div -> "/"
        }
    }
}

data class Literal<T>(val value: T) : RpnToken<T>()
sealed class RpnOperator<T> : RpnToken<T>() {
    class Plus<T> : RpnOperator<T>()
    class Minus<T> : RpnOperator<T>()
    class Mult<T> : RpnOperator<T>()
    class Div<T> : RpnOperator<T>()
}


private data class ShuntingYardState<T>(
        val rpnContext: RpnContext<T>,
        val operatorContext: RpnOperatorContext,
        val output: Deque<RpnToken<T>> = ArrayDeque(),
        val operators: Deque<ParsableRpnToken<T>> = ArrayDeque(),
        val literalBuilder: StringBuilder = StringBuilder()
) {
    fun addOperator(operator: RpnOperator<T>) {
        while (shouldPopOperator(operator)) {
            moveHeadOperatorToOutput()
        }
        operators.push(operator)
    }

    fun addRightParenthesis() {
        flushLiteral()
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

    fun addDigit(c: Char) {
        literalBuilder.append(c)
    }

    fun flushLiteral() {
        if (literalBuilder.isNotEmpty()) {
            rpnContext.run {
                output.add(Literal(literalBuilder.toString().parseLiteral()))
            }
            literalBuilder.clear()
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

    private fun shouldPopOperator(newOperator: RpnOperator<T>): Boolean = operatorContext.run {
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
                is Literal -> output.push(token.value)
                is RpnOperator -> {
                    if (output.isEmpty()) {
                        throw Error("Could not get right operand for ${token.asString()} in  $this")
                    }
                    val right = output.pop()
                    if (output.isEmpty()) {
                        throw Error("Could not get left operand for ${token.asString()} in  $this")
                    }
                    val left = output.pop()
                    rpnContext.run {
                        output.push(token.eval(left, right))
                    }
                }
            }
        }
        assert(output.size == 1) { "Output size is ${output.size}: ${output.toList()}. RPN is $this" }
        return output.pop()
    }

    val value: T by lazy {
        eval()
    }

    companion object {
        fun <T> parse(input: String, rpnContext: RpnContext<T>, operatorContext: RpnOperatorContext = defaultRpnOperatorContext): RPN<T> {
            val finalState = input.map { ParsableRpnToken.parse(it, rpnContext) }
                    .fold(ShuntingYardState<T>(rpnContext, operatorContext)) { state, token ->
                        when (token) {
                            is Digit -> state.addDigit(token.c)
                            is RpnOperator -> {
                                state.flushLiteral()
                                state.addOperator(token)
                            }
                            is LeftParenthesis -> {
                                state.flushLiteral()
                                state.operators.push(token)
                            }
                            is RightParenthesis -> state.addRightParenthesis()
                            is Whitespace -> {
                                state.flushLiteral()
                            }
                            is Literal -> {
                            }
                        }
                        state
                    }
            finalState.flushLiteral()
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

    override fun String.parseLiteral(): Long = this.toLong()
}

val defaultRpnOperatorContext = object : RpnOperatorContext {
    override fun RpnOperator<*>.priority(): Int = when (this) {
        is RpnOperator.Plus -> 1
        is RpnOperator.Minus -> 1
        is RpnOperator.Mult -> 2
        is RpnOperator.Div -> 2
    }
}