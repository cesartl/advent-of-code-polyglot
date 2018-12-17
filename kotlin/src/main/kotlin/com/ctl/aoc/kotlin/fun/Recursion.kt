package com.ctl.aoc.kotlin.`fun`

object Recursion {

    fun multiply(x: Int, y: Int): Int {
        if (x > y) return multiply(y, x)
        return when (x) {
            0 -> 0
            1 -> y
            else -> {
                val foo = multiply(x shr 1, y)
                return if (x % 2 == 0) {
                    foo + foo
                } else {
                    foo + foo + y
                }
            }
        }
    }

    fun parens(n: Int): List<String> {
        if (n == 0) return listOf("")
        val previous = parens(n - 1)
        val result = mutableListOf<String>()
        previous.forEach {
            result.add("($it)")
            result.add("()$it")
            result.add("$it()")
        }
        return result
    }
}