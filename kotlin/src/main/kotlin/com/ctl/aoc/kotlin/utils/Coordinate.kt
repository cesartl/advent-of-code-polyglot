package com.ctl.aoc.kotlin.utils

data class Coordinate(val x: Int, val y: Int, val z: Int){
    fun distance(other: Coordinate): Int = Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z- other.z)
}