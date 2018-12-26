package com.ctl.aoc.kotlin.utils

data class Coordinate(val x: Int, val y: Int, val z: Int){
    fun distance(other: Coordinate): Int = Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z- other.z)
}

data class Coordinate4D(val x: Int, val y: Int, val z: Int, val t: Int){
    fun distance(other: Coordinate4D): Int = Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z- other.z) + + Math.abs(t- other.t)
}