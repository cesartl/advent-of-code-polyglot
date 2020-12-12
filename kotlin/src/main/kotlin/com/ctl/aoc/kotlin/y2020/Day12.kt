package com.ctl.aoc.kotlin.y2020

import com.ctl.aoc.kotlin.utils.*

object Day12 {

    data class State(val position: Position, val orientation: Orientation)

    data class WaypointState(val relativePosition: Position)

    data class WithWaypoint(val waypointState: WaypointState, val position: Position)

    sealed class Command {
        data class N(val amount: Int) : Command()
        data class S(val amount: Int) : Command()
        data class E(val amount: Int) : Command()
        data class W(val amount: Int) : Command()
        data class L(val amount: Int) : Command()
        data class R(val amount: Int) : Command()
        data class F(val amount: Int) : Command()

        companion object {
            fun parse(s: String): Command {
                return when {
                    s.startsWith("N") -> {
                        N(s.drop(1).toInt())
                    }
                    s.startsWith("S") -> {
                        S(s.drop(1).toInt())
                    }
                    s.startsWith("E") -> {
                        E(s.drop(1).toInt())
                    }
                    s.startsWith("W") -> {
                        W(s.drop(1).toInt())
                    }
                    s.startsWith("L") -> {
                        L(s.drop(1).toInt())
                    }
                    s.startsWith("R") -> {
                        R(s.drop(1).toInt())
                    }
                    s.startsWith("F") -> {
                        F(s.drop(1).toInt())
                    }
                    else -> {
                        throw IllegalArgumentException(s)
                    }
                }
            }
        }
    }

    fun State.modify(command: Command): State {
        return when (command) {
            is Command.N -> this.copy(position = N.move(this.position, command.amount))
            is Command.S -> this.copy(position = S.move(this.position, command.amount))
            is Command.E -> this.copy(position = E.move(this.position, command.amount))
            is Command.W -> this.copy(position = W.move(this.position, command.amount))
            is Command.L -> this.copy(orientation = orientation.rotate(command.amount))
            is Command.R -> this.copy(orientation = orientation.rotate(-command.amount))
            is Command.F -> this.copy(position = orientation.move(position, command.amount))
        }
    }

    private fun WaypointState.rotateAround(degrees: Int): WaypointState {
        val (x, y) = this.relativePosition
        val m = when (val d = (360 + degrees) % 360) {
            0 -> Matrix22.identity
            90 -> Matrix22.rotate90
            180 -> Matrix22.rotate180
            270 -> Matrix22.rotate270
            else -> throw Error("degree: degrees ($d)")
        }
        val (newX, newY) = matrixMultiply(m, Matrix21(x, y))
        return WaypointState(Position(newX, newY))
    }

    fun WaypointState.modify(command: Command): WaypointState {
        val (x, y) = this.relativePosition
        return when (command) {
            is Command.N -> this.copy(relativePosition = N.moveTrigo(this.relativePosition, command.amount))
            is Command.S -> this.copy(relativePosition = S.moveTrigo(this.relativePosition, command.amount))
            is Command.E -> this.copy(relativePosition = E.moveTrigo(this.relativePosition, command.amount))
            is Command.W -> this.copy(relativePosition = W.moveTrigo(this.relativePosition, command.amount))
            is Command.L -> this.rotateAround(command.amount)
            is Command.R -> this.rotateAround(-command.amount)
            else -> throw Error("Illegal command  $command")
        }
    }

    fun WithWaypoint.modify(command: Command): WithWaypoint {
        return when (command) {
            is Command.N -> this.copy(waypointState = waypointState.modify(command))
            is Command.S -> this.copy(waypointState = waypointState.modify(command))
            is Command.E -> this.copy(waypointState = waypointState.modify(command))
            is Command.W -> this.copy(waypointState = waypointState.modify(command))
            is Command.L -> this.copy(waypointState = waypointState.modify(command))
            is Command.R -> this.copy(waypointState = waypointState.modify(command))
            is Command.F -> {
                val move = this.waypointState.relativePosition.scalar(command.amount)
                this.copy(position = position + move)
            }
        }
    }

    fun solve1(input: Sequence<String>): Int {
        val start = Position(0, 0)
        val endState = input.map { Command.parse(it) }
                .fold(State(start, E)) { state, command ->
                    state.modify(command)
                }
        println(endState)
        return endState.position.distance(start)
    }

    fun solve2(input: Sequence<String>): Int {
        val start = Position(0, 0)
        val waypointStart = Position(10, 1)
        val withWaypoint = WithWaypoint(WaypointState(waypointStart), start)

        val endState = input.map { Command.parse(it) }
                .fold(withWaypoint) { state, command -> state.modify(command) }
        println(endState)
        return endState.position.distance(start)
    }
}