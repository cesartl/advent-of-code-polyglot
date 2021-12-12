package com.ctl.aoc.kotlin.y2018


sealed class Direction {
    override fun toString(): String = this.javaClass.simpleName
}

object Up : Direction()
object Right : Direction()
object Down : Direction()
object Left : Direction()

fun Direction.turnLeft(): Direction {
    return when (this) {
        is Up -> Left   //  '^' ->  '<'
        is Left -> Down //  '<' -> 'v'
        is Down -> Right//  'v' -> '>'
        is Right -> Up  //  '>' -> '6'
    }
}

fun Direction.turnRight(): Direction {
    return when (this) {
        is Up -> Right
        is Right -> Down
        is Down -> Left
        is Left -> Up
    }
}

fun Direction.print(): String {
    return when (this) {
        Up -> "^"
        Right -> ">"
        Down -> "v"
        Left -> "<"
    }
}


sealed class TrackElement {
    override fun toString(): String = this.javaClass.simpleName
}

object Horizontal : TrackElement()
object Vertical : TrackElement()
object UpRightLeftDownTurn : TrackElement()
object UpLeftRightDownTurn : TrackElement()
object Intersection : TrackElement()

fun TrackElement.print(): String {
    return when (this) {
        Horizontal -> "-"
        Vertical -> "|"
        UpRightLeftDownTurn -> "/"
        UpLeftRightDownTurn -> "\\"
        Intersection -> "+"
    }
}

object Day13 {

    val intersectionDirections = listOf(Left, null, Right)

    data class Position(val x: Int, val y: Int)

    data class Cart(val id: Int, val direction: Direction, val nIntersection: Int = 0)

    data class Track(val trackMap: Map<Int, Map<Int, TrackElement>>) {
        fun trackElement(x: Int, y: Int): TrackElement? = trackMap[y]?.get(x)
    }

    fun parse(lines: List<String>): State {
        val trackMap = mutableMapOf<Int, MutableMap<Int, TrackElement>>()
        val cartMap = mutableMapOf<Int, MutableMap<Int, Cart>>()
        val size = 9999

        for (y in lines.indices) {
            val line = lines[y]
            val row = trackMap.computeIfAbsent(y) { mutableMapOf() }
            for (x in line.indices) {
                val id = y * size + x
                val char = line[x]
                val trackElement = when (char) {
                    '-', '<', '>' -> Horizontal
                    '|', '^', 'v' -> Vertical
                    '\\' -> UpLeftRightDownTurn
                    '/' -> UpRightLeftDownTurn
                    '+' -> Intersection
                    ' ' -> null
                    else -> throw IllegalArgumentException("$char")
                }
                if (trackElement != null) {
                    row[x] = trackElement
                }
                val cart = when (char) {
                    '>' -> Cart(id, Right)
                    '<' -> Cart(id, Left)
                    '^' -> Cart(id, Up)
                    'v' -> Cart(id, Down)
                    else -> null
                }
                if (cart != null) {
                    cartMap.computeIfAbsent(y) { mutableMapOf() }[x] = cart
                }
            }
        }
        val track = Track(trackMap)
        return State(track, cartMap, 0)
    }

    fun Direction.move(p: Position): Position {
        return when (this) {
            Up -> Position(p.x, p.y - 1)
            Right -> Position(p.x + 1, p.y)
            Down -> Position(p.x, p.y + 1)
            Left -> Position(p.x - 1, p.y)
        }
    }

    fun TrackElement.rotateChart(cart: Cart): Cart {
        return when (this) {
            is Horizontal, Vertical -> cart
            is UpRightLeftDownTurn -> { // '/'
                val direction = when (cart.direction) {
                    is Up, Down -> cart.direction.turnRight()
                    is Left, Right -> cart.direction.turnLeft()
                }
                return cart.copy(direction = direction)
            }
            is UpLeftRightDownTurn -> { // '\'
                val direction = when (cart.direction) {
                    is Up, Down -> cart.direction.turnLeft()
                    is Left, Right -> cart.direction.turnRight()
                }
                return cart.copy(direction = direction)
            }
            is Intersection -> {
                val choice = intersectionDirections[cart.nIntersection % intersectionDirections.size]
                val direction = when (choice) {
                    is Left -> cart.direction.turnLeft()
                    is Right -> cart.direction.turnRight()
                    else -> cart.direction
                }
                return cart.copy(direction = direction, nIntersection = cart.nIntersection + 1)
            }
        }
    }

    data class State(val track: Track, val carts: MutableMap<Int, MutableMap<Int, Cart>>, val tick: Int) {
        fun nextTick(destroyCart: Boolean = false): State {
//            println("next tick")
            val newMap = mutableMapOf<Int, MutableMap<Int, Cart>>()
            //filling new map with old value
            carts.forEach { y, row -> row.forEach { x, c -> newMap.computeIfAbsent(y) { mutableMapOf() }[x] = c } }
            var size = this.getCarts().size
            val crashed = mutableSetOf<Int>()
            carts.entries.sortedBy { it.key }.forEach { (y, row) ->
                //                println("starting row $y")
                row.entries.sortedBy { it.key }.forEach { (x, cart) ->
                    if (!crashed.contains(cart.id)) {
                        newMap[y]?.remove(x) //remove from old map
                        val newPosition = cart.direction.move(Position(x, y))
                        val trackElement = track.trackElement(newPosition.x, newPosition.y)
                                ?: throw IllegalArgumentException("no track at $newPosition")
                        val newCart = trackElement.rotateChart(cart)

                        val crash = newMap[newPosition.y]?.get(newPosition.x)

                        if (crash != null) {
                            println("crash")
                            if (!destroyCart) {
                                throw IllegalArgumentException("Collision $newPosition at $tick")
                            }
                            newMap[newPosition.y]?.remove(newPosition.x)
                            crashed.add(crash.id)
                        } else {
                            newMap.computeIfAbsent(newPosition.y) { mutableMapOf() }[newPosition.x] = newCart
                        }
                        //removing cart from old position
                    }
                }
            }
//            toBeRemoved.forEach { newMap[it.y]?.remove(it.x) }

            return State(track, newMap, tick + 1)
        }

        fun getCarts(): List<Pair<Position, Cart>> = carts.entries.flatMap { (y, row) -> row.entries.map { Position(it.key, y) to it.value } }

        fun print(): String {
            val builder = StringBuilder()
            val yMax = track.trackMap.keys.maxOrNull() ?: 0
            for (y in 0..yMax) {
                val row = track.trackMap[y]!!
                for (x in 0..(row.keys.maxOrNull() ?: 0)) {
                    builder.append(carts[y]?.get(x)?.direction?.print() ?: track.trackElement(x, y)?.print() ?: " ")
                }
                builder.append("\n")
            }
            return builder.toString()
        }
    }


    fun solve1(lines: List<String>): State {
        var state = parse(lines)
        for (i in 0 until 150000) {
            println(state.print())
            println("**")
            state = state.nextTick()
//            println(state.tick)
        }
        return state
    }

    fun debug(lines: List<String>, n: Int, destroy: Boolean): State {
        var state = parse(lines)
        for (i in 0 until n) {
            println(state.print())
            state = state.nextTick(destroy)
//            println(state.tick)
        }
        return state
    }

    fun solve2(lines: List<String>): Position {
        var state = parse(lines)
        var size = 999
        var newSize: Int
        while (state.getCarts().size > 1) {
//            println(state.getCarts().size)
//            println(state.print())
            newSize = state.getCarts().size
            if (newSize != size) {
                println("size $newSize")
                size = newSize
            }
//            println("size ${state.getCarts().size}")
            state = state.nextTick(true)
        }
//        println(state.print())
        return state.getCarts().first().first
    }


}