package com.ctl.aoc.kotlin.y2020

object Day8 {

    sealed class Opp {
        data class NoOp(val n: Int) : Opp()
        data class Acc(val n: Long) : Opp()
        data class Jump(val n: Int) : Opp()

        companion object {
            fun parse(s: String): Opp {
                return when {
                    s.contains("nop") -> {
                        NoOp(s.split(" ")[1].toInt())
                    }
                    s.contains("acc") -> {
                        Acc(s.split(" ")[1].toLong())
                    }
                    s.contains("jmp") -> {
                        Jump(s.split(" ")[1].toInt())
                    }
                    else -> throw IllegalArgumentException("s")
                }
            }
        }

        fun flip(): Opp {
            return when (this) {
                is NoOp -> Jump(this.n)
                is Acc -> this
                is Jump -> NoOp(this.n)
            }
        }
    }

    data class State(val acc: Long, val index: Int) {
        fun jump(n: Int = 1): State = copy(index = index + n)
        fun acc(n: Long): State = copy(acc = acc + n)
    }

    data class Program(val ops: List<Opp>) {
        fun next(state: State): State {
            return when (val opp = ops[state.index]) {
                is Opp.NoOp -> state.jump()
                is Opp.Acc -> state.acc(opp.n).jump()
                is Opp.Jump -> state.jump(opp.n)
            }
        }

        fun run(): State {
            val visited = mutableSetOf<Int>()
            var state = State(0, 0)
            var previous = state
            while (!visited.contains(state.index) && state.index < ops.size) {
                visited.add(state.index)
                previous = state
                state = next(state)
            }
            return previous
        }
    }


    fun solve1(input: Sequence<String>): Long {
        val p = Program(input.map { Opp.parse(it) }.toList())
        return p.run().acc
    }

    fun solve2(input: Sequence<String>): Long {
        val ops = input.map { Opp.parse(it) }.toList()
        ops.indices.filter { ops[it] !is Opp.Acc }.forEach { changeIdx ->
            val newOps = ops.mapIndexed { i, opp ->
                if (i == changeIdx) opp.flip() else opp
            }
            val p = Program(newOps)
            val r = p.run()
            if (r.index >= newOps.size - 1) {
                println("changed line $changeIdx")
                return r.acc
            }
        }
        throw IllegalArgumentException("Not found")
    }
}