package com.ctl.aoc.kotlin.y2015

object Day15 {

    data class Ingredient(val properties: List<Long>) {
        fun quatity(qty: Long): Ingredient = this.copy(properties = properties.map { it * qty })

        companion object {
            val regex = """[\w]+ ([\d-]+)""".toRegex()
            fun parse(s: String): Ingredient {
                return regex.findAll(s).map { it.groupValues[1].toLong() }.let { Ingredient(it.toList()) }
            }
        }
    }

    fun combine(l1: List<Long>, l2: List<Long>): List<Long> = l1.zip(l2).map { (l, r) -> l + r }

    fun combine(i1: Ingredient, i2: Ingredient): Ingredient {
        return Ingredient(combine(i1.properties, i2.properties))
    }

    fun zero(size: Int): List<Long> = generateSequence(0L) { 0L }.take(size).toList()

    fun add(l1: List<Long>, l2: List<Long>, modulo: Long = l1.size.toLong()): List<Long> {
        val x = l1.zip(l2).foldRightIndexed(zero(l1.size) to 0L) { idx, (l, r), (acc, carry) -> combine(acc, atIndex((l + r + carry) % modulo, idx, l1.size)) to ((l + r + carry) / modulo) }
        return x.first
    }

    fun mult(l: List<Long>, c: Long): List<Long> = l.map { it * c }

    fun atIndex(value: Long, idx: Int, size: Int): List<Long> = generateSequence(0L) { 0L }.take(size).mapIndexed { i, _ -> if (i == idx) value else 0L }.toList()


    data class Cookie(val quantities: List<Long>, val ingredients: List<Ingredient>) {
        val score1: Long = quantities.zip(ingredients).map { (qty, ingredient) -> ingredient.quatity(qty) }.reduce { l, r -> combine(l, r) }.properties.dropLast(1).fold(1L) { acc, l -> acc * Math.max(0, l) }
        val calories: Long = quantities.zip(ingredients).map { (qty, ingredient) -> ingredient.quatity(qty) }.reduce { l, r -> combine(l, r) }.properties.last()
        val total = quantities.sum()
        override fun toString(): String {
            return "Cookie(quantities=$quantities, score1=$score1, total=$total, calories=$calories)"
        }
    }


    fun generateAll(size: Int, max: Long): Sequence<List<Long>> = sequence {
        val one = atIndex(1, size - 1, size)
        var quantities = zero(size)
        val upper = Math.pow(max.toDouble() + 1, quantities.size.toDouble()).toLong()
        for (i in 1 until upper) {
            if (i % 100000L == 0L) {
                println("i: $i")
            }
            quantities = add(quantities, one, max + 1)
            yield(quantities)
        }
    }

    tailrec fun findStable(ingredients: List<Ingredient>, max: Long = 1): Cookie {
        return generateAll(ingredients.size, max).map { Cookie(it, ingredients) }.filter { it.score1 > 0 }.firstOrNull()
                ?: findStable(ingredients, max + 1)
    }

    fun scaleUpTo(cookie: Cookie, n: Int): Cookie {
        var cookie = findStable(cookie.ingredients)
        var current: Cookie = cookie
        var i = 2L
        while (current.quantities.sum() < n) {
            current = current.copy(quantities = mult(cookie.quantities, i))
            i++
        }
        println("i $i")
        return cookie.copy(quantities = mult(cookie.quantities, i - 2))
    }

    fun solve1(lines: Sequence<String>): Cookie {
        val ingredients = lines.map { Day15.Ingredient.parse(it) }.toList()
        println(ingredients)

        var cookie = scaleUpTo(findStable(ingredients), 100)

        println("start $cookie")
        println("total ${cookie.quantities.sum()}")
        var total = cookie.quantities.sum()
        while (total < 100) {
            val candidates = generateSequence(cookie.quantities) { l -> l }.mapIndexed { index, list -> combine(list, atIndex(1, index, ingredients.size)) }.map { Cookie(it, ingredients) }.take(ingredients.size).sortedBy { it.score1 }.toList()
            println(candidates)
            cookie = candidates.last()
            total++
        }
        println(cookie)
        return cookie
    }

    fun solve2(lines: Sequence<String>): Cookie {
        val ingredients = lines.map { Day15.Ingredient.parse(it) }.toList()
        val stable = findStable(ingredients)
        println("stable $stable")
        var cookie = scaleUpTo(stable, 80)

        println("cookie $cookie")
        while (cookie.total < 100) {
            val candidates = generateAll(ingredients.size, Math.min(30, 100 - cookie.total)).map { Cookie(combine(cookie.quantities, it), ingredients) }.filter { it.quantities.sum() <= 100 }.filter { it.score1 > 0 }.filter { it.calories == 500L }.sortedBy { it.score1 }.toList()
            println("candidates ${candidates.size}")
            cookie = candidates.last()
            println("cookie $cookie")
        }
        println(cookie)
        return cookie
    }
}