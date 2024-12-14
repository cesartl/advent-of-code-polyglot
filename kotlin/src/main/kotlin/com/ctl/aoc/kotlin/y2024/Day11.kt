package com.ctl.aoc.kotlin.y2024

object Day11 {
    fun solve1(input: Sequence<String>): Long {
        return blink(input, 25)
    }

    private fun blink(input: Sequence<String>, n: Int): Long {
        return input.map { countStones(it, n) }.sum()
    }

    private val cache = mutableMapOf<Pair<String, Int>, Long>()

    private fun countStones(stone: String, remaining: Int): Long{
        val cached = cache[Pair(stone, remaining)]
        if(cached != null){
            return cached
        }
        if(remaining == 0 ){
            return 1
        }
        if(stone == "0"){
            return countStones("1", remaining - 1).let {
                cache[Pair(stone, remaining)] = it
                it
            }
        }
        if(stone.length % 2 == 0){
            val firstHalf = stone.substring(0, stone.length / 2).toLong().toString()
            val secondHalf = stone.substring(stone.length / 2).toLong().toString()
            return (countStones(firstHalf, remaining -1) + countStones(secondHalf, remaining - 1)).let {
                cache[Pair(stone, remaining)] = it
                it
            }
        }
        val stoneValue = stone.toLong()
        return countStones((stoneValue * 2024).toString(), remaining - 1).let {
            cache[Pair(stone, remaining)] = it
            it
        }
    }

    fun solve2(input: Sequence<String>): Long {
        return blink(input, 75)
    }
}
