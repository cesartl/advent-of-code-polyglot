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
            is Number -> this.n.toString()+"L"
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


    fun solve1(input: Sequence<String>): Int {
//        val instructions = input.map { Instr.parse(it) }.toList()
//        val kotlin = kotlin(instructions)
//        println(kotlin)
        var c = 0L
        (99999999999999L downTo 11111111111111L).forEach { c++ }
        println(c)
        TODO()
    }

    fun run(input: List<Long>): Long {
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
        return z
    }


    fun solve2(input: Sequence<String>): Int {
        TODO()
    }
}