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


    data class Cookie(val quantities: List<Long>, val ingredients: Iterable<Ingredient>) {
        val score1: Long = quantities.zip(ingredients).map { (qty, ingredient) -> ingredient.quatity(qty) }.reduce { l, r -> combine(l, r) }.properties.dropLast(1).fold(1L) { acc, l -> acc * Math.max(0, l) }
        override fun toString(): String {
            return "Cookie(quantities=$quantities, score1=$score1)"
        }
    }



    tailrec fun findStable(ingredients: List<Ingredient>, max: Long = 1): Cookie {
        val one = atIndex(1, ingredients.size - 1, ingredients.size)
        println("max $max")

        var quantities = zero(ingredients.size)
        val upper = Math.pow(quantities.size.toDouble(), max.toDouble() + 1).toInt()
        for (i in 1..upper) {
            quantities = add(quantities, one, max + 1)
            val cookie = Cookie(quantities, ingredients)
            if (cookie.score1 > 0) {
                println("found stable $cookie")
                return cookie
            }
        }
        return findStable(ingredients, max + 1)
    }

    fun solve1(lines: Sequence<String>): Cookie {
        val ingredients = lines.map { Day15.Ingredient.parse(it) }.toList()
        println(ingredients)

        var cookie = findStable(ingredients)
        var current: Cookie = cookie
        var i = 2L
        while (current.quantities.sum() <= 100) {
            current = current.copy(quantities = mult(current.quantities, i))
            i++
        }
        println("i $i")
        cookie = cookie.copy(quantities = mult(cookie.quantities, i))


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
}