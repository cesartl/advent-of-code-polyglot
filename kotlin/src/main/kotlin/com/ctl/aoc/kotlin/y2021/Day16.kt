package com.ctl.aoc.kotlin.y2021

object Day16 {

    @JvmInline
    value class BinaryString(val binary: String = "") {
        val length: Int
            get() = binary.length
    }

    fun BinaryString.toLong(): Long = binary.toLong(2)
    fun BinaryString.startsWith(c: Char): Boolean = binary.startsWith(c)
    operator fun BinaryString.plus(other: BinaryString): BinaryString = BinaryString(this.binary + other.binary)
    fun BinaryString.drop(n: Long) = BinaryString(binary.drop(1))

    fun String.toBinary(): BinaryString = this.splitToSequence("").filter { it != "" }.joinToString(separator = "") { Integer.toBinaryString(Integer.parseInt(it, 16)).padStart(4, '0') }.run { BinaryString(this) }

    @JvmInline
    value class Version(val binary: BinaryString)

    @JvmInline
    value class TypeId(val binary: BinaryString) {
        val type: Long
            get() = binary.toLong()
    }

    sealed class Packet {
        abstract val version: Version
        abstract val typeId: TypeId
        abstract val source: BinaryString

        data class Literal(val literalValue: BinaryString, override val version: Version, override val typeId: TypeId, override val source: BinaryString) : Packet() {
            val numeric: Long = literalValue.toLong()
            override fun toString(): String {
                return "Literal(source=$source, numeric=$numeric)"
            }

        }

        data class Operator(val subPackets: List<Packet>, override val version: Version, override val typeId: TypeId, override val source: BinaryString) : Packet() {
            override fun toString(): String {
                return "Operator(subPackets=$subPackets, typeId=$typeId)"
            }
        }
    }

    fun Packet.versionSum(): Long {
        return when (this) {
            is Packet.Literal -> this.version.binary.toLong()
            is Packet.Operator -> this.version.binary.toLong() + subPackets.sumOf { it.versionSum() }
        }
    }

    fun Packet.run(): Long {
        return when (this) {
            is Packet.Literal -> this.numeric
            is Packet.Operator -> {
                when (this.typeId.type) {
                    0L -> this.subPackets.sumOf { it.run() }
                    1L -> this.subPackets.map { it.run() }.fold(1L) { x, y -> x * y }
                    2L -> this.subPackets.minOf { it.run() }
                    3L -> this.subPackets.maxOf { it.run() }
                    5L -> if (this.subPackets[0].run() > this.subPackets[1].run()) 1 else 0
                    6L -> if (this.subPackets[0].run() < this.subPackets[1].run()) 1 else 0
                    7L -> if (this.subPackets[0].run() == this.subPackets[1].run()) 1 else 0
                    else -> error("unknown type ${this.typeId.type}")
                }
            }
        }
    }

    fun Packet.toKotlin(): String {
        return when (this) {
            is Packet.Literal -> this.numeric.toString() + "L"
            is Packet.Operator -> {
                when (this.typeId.type) {
                    0L -> this.subPackets.joinToString(prefix = "(", postfix = ")", separator = " + ") { it.toKotlin() }
                    1L -> this.subPackets.joinToString(prefix = "(", postfix = ")", separator = " * ") { it.toKotlin() }
                    2L -> this.subPackets.joinToString(prefix = "listOf(", postfix = ").minOrNull()!!", separator = ",") { it.toKotlin() }
                    3L -> this.subPackets.joinToString(prefix = "listOf(", postfix = ").maxOrNull()!!", separator = ",") { it.toKotlin() }
                    5L -> "if(${this.subPackets[0].toKotlin()} > ${this.subPackets[1].toKotlin()}) 1L else 0L"
                    6L -> "if(${this.subPackets[0].toKotlin()} < ${this.subPackets[1].toKotlin()}) 1L else 0L"
                    7L -> "if(${this.subPackets[0].toKotlin()} == ${this.subPackets[1].toKotlin()}) 1L else 0L"
                    else -> error("unknown type ${this.typeId.type}")
                }
            }
        }
    }

    class Scanner(private val source: BinaryString) {
        private var start = 0
        private var current = 0

        fun scanPacket(): Packet {
            return parsePacket()
        }

        private fun parsePacket(): Packet {
            start = current
            val version = parseVersion()
            val typeId = parseTypeId()
            return if (typeId.type == 4L) {
                parseLiteral(version, typeId)
            } else {
                parseOperator(version, typeId)
            }
        }

        private fun parseLiteral(version: Version, typeId: TypeId): Packet.Literal {
            var group: BinaryString
            var literalValue = BinaryString()
            do {
                group = advance(5)
                literalValue += group.drop(1)
            } while (group.startsWith('1'))
            val packetSource = BinaryString(source.binary.substring(start, current))
            return Packet.Literal(literalValue, version, typeId, packetSource)
        }

        private fun parseOperator(version: Version, typeId: TypeId): Packet.Operator {
            val operatorStart = start
            val mode = advance()
            if (mode == '0') {
                val nBits = advance(15).toLong()
                val subPackets = mutableListOf<Packet>()
                while (subPackets.sumOf { it.source.length } < nBits.toLong()) {
                    subPackets.add(parsePacket())
                }
                val packetSource = BinaryString(source.binary.substring(operatorStart, current))
                return Packet.Operator(subPackets, version, typeId, packetSource)
            } else {
                val nPackets = advance(11).toLong()
                val subPackets = mutableListOf<Packet>()
                while (subPackets.size < nPackets) {
                    subPackets.add(parsePacket())
                }
                val packetSource = BinaryString(source.binary.substring(operatorStart, current))
                return Packet.Operator(subPackets, version, typeId, packetSource)
            }
        }

        fun parseVersion(): Version = Version(advance(3))
        fun parseTypeId(): TypeId = TypeId(advance(3))

        private fun advance(): Char = source.binary[current++]
        private fun advance(n: Int): BinaryString = generateSequence(advance()) { advance() }.take(n).joinToString(separator = "").run { BinaryString(this) }
    }

    fun BinaryString.parse(): Packet {
        return Scanner(this).scanPacket()
    }

    fun solve1(input: String): Long {
        return input.toBinary().parse().versionSum()
    }

    fun solve2(input: String): Long {
        return input.toBinary().parse().run()
    }
}