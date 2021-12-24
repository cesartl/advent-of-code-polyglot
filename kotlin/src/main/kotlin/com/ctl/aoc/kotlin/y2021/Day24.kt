package com.ctl.aoc.kotlin.y2021

object Day24 {

    sealed class Ref {
        data class Register(val name: Char) : Ref()
        data class Number(val n: Long) : Ref()
        companion object {
            fun parse(s: String): Ref {
                return s.toLongOrNull()?.let { Number(it) } ?: Register(s.first())
            }
        }

        fun toKotlin(): String = when (this) {
            is Number -> this.n.toString() + "L"
            is Register -> this.name.toString()
        }
    }

    sealed class Instr {
        data class Inp(val v: Char) : Instr()
        sealed class BinaryOp() : Instr() {
            abstract val a: Ref
            abstract val b: Ref
        }

        data class Add(override val a: Ref, override val b: Ref) : BinaryOp()
        data class Mul(override val a: Ref, override val b: Ref) : BinaryOp()
        data class Div(override val a: Ref, override val b: Ref) : BinaryOp()
        data class Mod(override val a: Ref, override val b: Ref) : BinaryOp()
        data class Eql(val a: Ref, val b: Ref) : Instr()

        companion object {
            fun parse(s: String): Instr {
                val parts = s.split(" ")
                return when {
                    s.startsWith("inp") -> {
                        Inp(parts[1].first())
                    }
                    s.startsWith("add") -> {
                        Add(Ref.parse(parts[1]), Ref.parse(parts[2]))
                    }
                    s.startsWith("mul") -> {
                        Mul(Ref.parse(parts[1]), Ref.parse(parts[2]))
                    }
                    s.startsWith("div") -> {
                        Div(Ref.parse(parts[1]), Ref.parse(parts[2]))
                    }
                    s.startsWith("mod") -> {
                        Mod(Ref.parse(parts[1]), Ref.parse(parts[2]))
                    }
                    s.startsWith("eql") -> {
                        Eql(Ref.parse(parts[1]), Ref.parse(parts[2]))
                    }
                    else -> {
                        error(s)
                    }
                }
            }
        }
    }

    fun Instr.toKotlin(): String {
        return when (this) {
            is Instr.Inp -> "${this.v}=input[idx++]"
            is Instr.Eql -> "${this.a.toKotlin()}=if(${this.a.toKotlin()}==${this.b.toKotlin()}) 1L else 0L"
            is Instr.BinaryOp -> {
                val opp = when (this) {
                    is Instr.Add -> "+"
                    is Instr.Div -> "/"
                    is Instr.Mod -> "%"
                    is Instr.Mul -> "*"
                }
                "${this.a.toKotlin()}=${this.a.toKotlin()}$opp${this.b.toKotlin()}"
            }
        }
    }

    fun kotlin(instructions: List<Instr>): String {
        val builder = StringBuilder()
        builder.append("fun run(input: List<Long>): Long {\n")
        builder.append("\tvar idx = 0\n")
        builder.append("\tvar w = 0L\n")
        builder.append("\tvar x = 0L\n")
        builder.append("\tvar y = 0L\n")
        builder.append("\tvar z = 0L\n")
        instructions.forEach { builder.append("\t${it.toKotlin()}\n") }
        builder.append("\treturn z\n")
        builder.append("}")
        return builder.toString()
    }

    fun chunkProgram(a: Long, b: Long, c: Long): (Long, Long) -> Long = { z, w ->
        var zz = z
        val x = b + z % 26
        zz /= a
        if (x != w) {
            zz *= 26
            zz += w + c
        }
        zz
    }

    data class ChunkParam(val a: Long, val b: Long, val c: Long)

    data class Eq(val i1: Int, val i2: Int, val offset: Long)

    fun solve1(input: Sequence<String>): Long {
        val blocks = input.chunked(18).toList()
        val chunks = blocks.map { instrs ->
            val l = mutableListOf<String>()
            l.add(instrs[4])
            l.add(instrs[5])
            l.add(instrs[15])
            l
        }.map { (aa, bb, cc) ->
            val a = aa.split(" ")[2].toLong()
            val b = bb.split(" ")[2].toLong()
            val c = cc.split(" ")[2].toLong()
            ChunkParam(a, b, c)
        }
        val a1 = chunks.filter { (a, b, c) -> a == 1L }
        val a26 = chunks.filter { (a, b, c) -> a == 26L }

        val (stack, eqs) = chunks.foldIndexed(listOf<Pair<Int, Long>>() to listOf<Eq>()) { i, (stack, eqs), chunk ->
            if (chunk.a == 1L) {
                (listOf(i to chunk.c)) + stack to eqs
            } else {
                val (previousIndex, c) = stack.first()
                stack.drop(1) to eqs + Eq(i, previousIndex, chunk.b + c)
            }
        }
        val found = generateModelNumber().map { l -> l.joinToString(separator = "") to run(l.map { it.toLong() }) }
            .find { it.second == 0L }
        println()
        return found!!.first.toLong()
    }

    fun generateModelNumber(): Sequence<List<Int>> {
        return sequence {
            (4 downTo 1).forEach { w0 ->
                (9 downTo 1).forEach { w1 ->
                    (9 downTo 9).forEach { w2 ->
                        (7 downTo 1).forEach { w4 ->
                            (2 downTo 1).forEach { w6 ->
                                (9 downTo 6).forEach { w7 ->
                                    (9 downTo 7).forEach { w8 ->
                                        val w3 = w2 - 8
                                        val w5 = w4 + 2
                                        val w9 = w8 - 6
                                        val w10 = w7 - 5
                                        val w11 = w6 + 7
                                        val w12 = w1
                                        val w13 = w0 + 5
                                        yield(listOf(w0, w1, w2, w3, w4, w5, w6, w7, w8, w9, w10, w11, w12, w13))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun generateModelNumber2(): Sequence<List<Int>> {
        return sequence {
            (4 downTo 1).reversed().forEach { w0 ->
                (9 downTo 1).reversed().forEach { w1 ->
                    (9 downTo 9).reversed().forEach { w2 ->
                        (7 downTo 1).reversed().forEach { w4 ->
                            (2 downTo 1).reversed().forEach { w6 ->
                                (9 downTo 6).reversed().forEach { w7 ->
                                    (9 downTo 7).reversed().forEach { w8 ->
                                        val w3 = w2 - 8
                                        val w5 = w4 + 2
                                        val w9 = w8 - 6
                                        val w10 = w7 - 5
                                        val w11 = w6 + 7
                                        val w12 = w1
                                        val w13 = w0 + 5
                                        yield(listOf(w0, w1, w2, w3, w4, w5, w6, w7, w8, w9, w10, w11, w12, w13))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun run(input: List<Long>): Long {
        println(input.joinToString(""))
        var idx = 0
        var w = 0L
        var x = 0L
        var y = 0L
        var z = 0L
        w=input[idx++]
        x=x*0L
        x=x+z
        x=x%26L
        z=z/1L
        x=x+15L
        x=if(x==w) 1L else 0L
        x=if(x==0L) 1L else 0L
        y=y*0L
        y=y+25L
        y=y*x
        y=y+1L
        z=z*y
        y=y*0L
        y=y+w
        y=y+15L
        y=y*x
        z=z+y
        w=input[idx++]
        x=x*0L
        x=x+z
        x=x%26L
        z=z/1L
        x=x+12L
        x=if(x==w) 1L else 0L
        x=if(x==0L) 1L else 0L
        y=y*0L
        y=y+25L
        y=y*x
        y=y+1L
        z=z*y
        y=y*0L
        y=y+w
        y=y+5L
        y=y*x
        z=z+y
        w=input[idx++]
        x=x*0L
        x=x+z
        x=x%26L
        z=z/1L
        x=x+13L
        x=if(x==w) 1L else 0L
        x=if(x==0L) 1L else 0L
        y=y*0L
        y=y+25L
        y=y*x
        y=y+1L
        z=z*y
        y=y*0L
        y=y+w
        y=y+6L
        y=y*x
        z=z+y
        w=input[idx++]
        x=x*0L
        x=x+z
        x=x%26L
        z=z/26L
        x=x+-14L
        x=if(x==w) 1L else 0L
        x=if(x==0L) 1L else 0L
        y=y*0L
        y=y+25L
        y=y*x
        y=y+1L
        z=z*y
        y=y*0L
        y=y+w
        y=y+7L
        y=y*x
        z=z+y
        w=input[idx++]
        x=x*0L
        x=x+z
        x=x%26L
        z=z/1L
        x=x+15L
        x=if(x==w) 1L else 0L
        x=if(x==0L) 1L else 0L
        y=y*0L
        y=y+25L
        y=y*x
        y=y+1L
        z=z*y
        y=y*0L
        y=y+w
        y=y+9L
        y=y*x
        z=z+y
        w=input[idx++]
        x=x*0L
        x=x+z
        x=x%26L
        z=z/26L
        x=x+-7L
        x=if(x==w) 1L else 0L
        x=if(x==0L) 1L else 0L
        y=y*0L
        y=y+25L
        y=y*x
        y=y+1L
        z=z*y
        y=y*0L
        y=y+w
        y=y+6L
        y=y*x
        z=z+y
        w=input[idx++]
        x=x*0L
        x=x+z
        x=x%26L
        z=z/1L
        x=x+14L
        x=if(x==w) 1L else 0L
        x=if(x==0L) 1L else 0L
        y=y*0L
        y=y+25L
        y=y*x
        y=y+1L
        z=z*y
        y=y*0L
        y=y+w
        y=y+14L
        y=y*x
        z=z+y
        w=input[idx++]
        x=x*0L
        x=x+z
        x=x%26L
        z=z/1L
        x=x+15L
        x=if(x==w) 1L else 0L
        x=if(x==0L) 1L else 0L
        y=y*0L
        y=y+25L
        y=y*x
        y=y+1L
        z=z*y
        y=y*0L
        y=y+w
        y=y+3L
        y=y*x
        z=z+y
        w=input[idx++]
        x=x*0L
        x=x+z
        x=x%26L
        z=z/1L
        x=x+15L
        x=if(x==w) 1L else 0L
        x=if(x==0L) 1L else 0L
        y=y*0L
        y=y+25L
        y=y*x
        y=y+1L
        z=z*y
        y=y*0L
        y=y+w
        y=y+1L
        y=y*x
        z=z+y
        w=input[idx++]
        x=x*0L
        x=x+z
        x=x%26L
        z=z/26L
        x=x+-7L
        x=if(x==w) 1L else 0L
        x=if(x==0L) 1L else 0L
        y=y*0L
        y=y+25L
        y=y*x
        y=y+1L
        z=z*y
        y=y*0L
        y=y+w
        y=y+3L
        y=y*x
        z=z+y
        w=input[idx++]
        x=x*0L
        x=x+z
        x=x%26L
        z=z/26L
        x=x+-8L
        x=if(x==w) 1L else 0L
        x=if(x==0L) 1L else 0L
        y=y*0L
        y=y+25L
        y=y*x
        y=y+1L
        z=z*y
        y=y*0L
        y=y+w
        y=y+4L
        y=y*x
        z=z+y
        w=input[idx++]
        x=x*0L
        x=x+z
        x=x%26L
        z=z/26L
        x=x+-7L
        x=if(x==w) 1L else 0L
        x=if(x==0L) 1L else 0L
        y=y*0L
        y=y+25L
        y=y*x
        y=y+1L
        z=z*y
        y=y*0L
        y=y+w
        y=y+6L
        y=y*x
        z=z+y
        w=input[idx++]
        x=x*0L
        x=x+z
        x=x%26L
        z=z/26L
        x=x+-5L
        x=if(x==w) 1L else 0L
        x=if(x==0L) 1L else 0L
        y=y*0L
        y=y+25L
        y=y*x
        y=y+1L
        z=z*y
        y=y*0L
        y=y+w
        y=y+7L
        y=y*x
        z=z+y
        w=input[idx++]
        x=x*0L
        x=x+z
        x=x%26L
        z=z/26L
        x=x+-10L
        x=if(x==w) 1L else 0L
        x=if(x==0L) 1L else 0L
        y=y*0L
        y=y+25L
        y=y*x
        y=y+1L
        z=z*y
        y=y*0L
        y=y+w
        y=y+1L
        y=y*x
        z=z+y
        println(z)
        return z
    }


    fun solve2(input: Sequence<String>): Long {
        val found = generateModelNumber2().map { l -> l.joinToString(separator = "") to run(l.map { it.toLong() }) }
            .find { it.second == 0L }
        println()
        return found!!.first.toLong()
    }
}