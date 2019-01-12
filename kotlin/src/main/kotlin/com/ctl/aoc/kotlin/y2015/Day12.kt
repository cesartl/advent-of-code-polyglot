package com.ctl.aoc.kotlin.y2015

object Day12 {

    sealed class Json<out A> {
        object JNull : Json<Any>()
        data class JBool(val value: Boolean) : Json<Nothing>()
        data class JNum(val value: Double) : Json<Nothing>()
        data class JStrong(val value: String) : Json<Nothing>()
        data class JArray<A>(val value: List<Json<A>>) : Json<A>()
        data class JObject<A>(val value: Map<String, Json<A>>)
    }

}