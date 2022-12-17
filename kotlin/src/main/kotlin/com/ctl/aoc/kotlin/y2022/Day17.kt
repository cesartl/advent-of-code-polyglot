package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.Position
import kotlin.math.absoluteValue

object Day17 {

    sealed class Shape {

        fun pattern(): List<Position> = when (this) {
            HLine -> listOf(Position(0, 0), Position(1, 0), Position(2, 0), Position(3, 0))
            Cross -> listOf(Position(0, -1), Position(1, 0), Position(1, -1), Position(1, -2), Position(2, -1))
            L -> listOf(Position(0, 0), Position(1, 0), Position(2, 0), Position(2, -1), Position(2, -2))
            Square -> listOf(Position(0, 0), Position(1, 0), Position(1, -1), Position(0, -1))
            VLine -> listOf(Position(0, 0), Position(0, -1), Position(0, -2), Position(0, -3))
        }

        fun spwan(at: Position): List<Position> {
            return pattern().map { it + at }
        }

        object HLine : Shape()
        object Cross : Shape()
        object L : Shape()
        object VLine : Shape()
        object Square : Shape()

    }

    private fun Char.moveOffset(): Position = when (this) {
        '>' -> Position(1, 0)
        '<' -> Position(-1, 0)
        else -> error(this)
    }

    data class Tetris(
        val blocks: MutableSet<Position>,
        val xBoundary: IntRange = (1..7)
    ) {

        fun height(): Long {
            return blocks.minBy { it.y }.y.absoluteValue.toLong()
        }

        fun addShape(shape: Shape, gas: Iterator<Char>) {
            val minY = blocks.minByOrNull { it.y }?.y ?: 0
            val spawnAt = Position(3, minY - 4)
//            println("spawnAt $spawnAt")
            val newBlocks = shape.spwan(spawnAt)
            var current = newBlocks
            while (current.intersect(blocks).isEmpty()) {
                current = move(current, gas)
            }
            current = current.map { it + Position(0, -1) }
            blocks.addAll(current)
        }

        private fun move(newBlocks: List<Position>, gas: Iterator<Char>): List<Position> {
            val char = gas.next()
//            println("moving $char")
            val moveOffset = char.moveOffset()
            val afterGas = newBlocks.map { it + moveOffset }
            return if (afterGas.all { it.x in xBoundary } && afterGas.intersect(blocks).isEmpty()) {
//                println("moving")
                afterGas
            } else {
//                println("nothing")
                newBlocks
            }.map { it + Position(0, 1) }
        }

        fun print() {
            val minY = blocks.minByOrNull { it.y }?.y ?: 0
            (minY..0).forEach { y ->
                (0..8).forEach { x ->
                    if (y == 0) {
                        print("_")
                    } else if (x == 0 || x == 8) {
                        print("|")
                    } else if (blocks.contains(Position(x, y))) {
                        print("#")
                    } else {
                        print(".")
                    }
                }
                println("")
            }
        }

    }

    private fun gasIterator(input: String): Iterator<Char> {
        return generateSequence(input) { input }
            .flatMap { it.asSequence() }
            .iterator()
    }

    private fun shapeSequence(): Sequence<Shape> {
        val order = listOf(Shape.HLine, Shape.Cross, Shape.L, Shape.VLine, Shape.Square)
        return generateSequence(order) { order }
            .flatMap { it }
    }

    fun solve1(input: String): Int {
        val gas = gasIterator(input.trim())
        val tetris = Tetris((1..7).map { Position(it, 0) }.toMutableSet())
        shapeSequence()
            .take(2022)
            .forEach {
                tetris.addShape(it, gas)
            }
        tetris.print()
        return tetris.height().toInt()
    }

    fun solve2(input: String): Long {
        val diffs = generateHeightDiffString(input)


        val pattern1 = "01230113220023401212012120132001334"
        val start1 = "01230113220023401212012120132001334"

        val start2 =
            "222013322130201220013320133001320213322121200332002042033201302213022132421213013201121401330002222132001124013300123011322212132132201330203300101421303212240133001332013212122"
        val pattern2 =
            "4002340023021230013220103001322013200132111332002322133221324213300133200224013340023201214010320123220234013022121221222012220022001302013340123421232013340132121302013322133420000103300133221321213012003201330013300003100334013342122221334213210132001324002240130221304003030023401230002302130401230012300123421212003300133401322012130022101322013300133001030010311103121232213220130121330013020122221334012122002200232212302102400022002300112100330012140133001321003002023401322213212132221324010020130201330212122132201321013302132221210012302022001332012300133201232213222133021304002340023221030013220133201330213300103000212012120123010321113300122421222012120130401220202322133201330213300132201332013322133220030213320022220034213340133221321012220133201213013240003221321212132030301222002322132221213013300132201230103300133000232213200133221230201220133201322013002130420220213020133001334212340030321212013300022221322002100133020301213202133001232013320130201324012130132221212013300123021332212120122401332002200132221322013030123401322013220132101031010311112401332013022133001321113222132421304013320133001212213320133021304003032133220302212300023011230112340132111224012120132221302012122132401004213202133221320212222122111224013020133221232210320133000332013320132000230012120132201322012212133001332013032133401213213320023001322013040133001332003222130300320202222132200234013300133001303212302133021321103020133201330013300103401212213320133421303003322103101224013320022201302200342123201330013011103401332002300133201332213300012201121013040020200132002300121421230213030133000222013300133420300013322123021332212210133201324213300103401330013340022001330013342133200032012200122401330013300133"

        val pattern = pattern2
        val start = start2

        val l = 1000000000000L
        val n = (l - start.length) / pattern.length
        val r = (l - start.length) % pattern.length
        val startHeight = start.map { it.toString().toLong() }.sum()
        val patternHeight = pattern.map { it.toString().toLong() }.sum()
        val reminder = pattern.take(r.toInt()).map { it.toString().toLong() }.sum()
        return startHeight + patternHeight * n + reminder + 1
    }

    private fun generateHeightDiffString(input: String): Sequence<Long> {
        val gas = gasIterator(input.trim())
        val tetris = Tetris((1..7).map { Position(it, 0) }.toMutableSet())
        return shapeSequence()
            .map {
                tetris.addShape(it, gas)
                tetris.height()
            }
            .zipWithNext { a, b -> b - a }
    }
}
