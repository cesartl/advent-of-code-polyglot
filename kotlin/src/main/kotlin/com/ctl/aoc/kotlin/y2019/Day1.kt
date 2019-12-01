package com.ctl.aoc.kotlin.y2019

object Day1 {

    fun part1(modules: Sequence<Long>): Long {
        return modules.map { it / 3 - 2 }.sum()
    }

    fun part2(modules: Sequence<Long>): Long {
        return modules.map { fuelForModule(it) }.sum()
    }

    private fun fuelForModule(mass: Long): Long {
        var init = mass / 3 -2
        var fuel = init / 3 - 2
        while (fuel > 0) {
            init += fuel
            fuel = fuel / 3 - 2
        }
        return init
    }


}