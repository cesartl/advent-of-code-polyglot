package com.ctl.aoc.kotlin.y2019.day13

import com.ctl.aoc.kotlin.utils.InputUtils
import com.ctl.aoc.kotlin.y2019.Day13
import com.ctl.aoc.kotlin.y2019.Day13.GameState
import com.ctl.aoc.kotlin.y2019.Day13.Point
import com.ctl.aoc.kotlin.y2019.Day13.Tile
import javafx.beans.property.*
import javafx.collections.FXCollections
import javafx.scene.paint.Color
import tornadofx.*

const val size = 20

val intCode = InputUtils.getString(2019, 13).split(",").map { it.toLong() }.toLongArray()

fun dummyState(): GameState {
    val grid = mutableMapOf<Point, Tile>()
    grid[Point(0, 0)] = Tile.Wall
    grid[Point(40, 20)] = Tile.Wall
    grid[Point(5, 0)] = Tile.Block
    grid[Point(5, 5)] = Tile.Ball
    grid[Point(5, 20)] = Tile.Paddle
    return GameState(grid)
}

object StartGameEvent : FXEvent(EventBus.RunOn.BackgroundThread)

class GameStateEvent(val gameState: GameState) : FXEvent()

//class GameController : Controller() {
//
//    init {
//        println("Initialising game controller")
//        subscribe<StartGameEvent> {
//            println("StartGameEvent received")
//            Day13.run {
//                runGame(intCode) {
//                    fire(GameStateEvent(it))
//                }
//            }
//        }
//    }
//}

class GameStateViewModel : ItemViewModel<GameState>() {
    val grid: MapProperty<Point, Tile> = bind {
        SimpleMapProperty(FXCollections.observableMap(item?.grid ?: mapOf()))
    }

    val list: ListProperty<Pair<Point, Tile>> = bind {
        SimpleListProperty(FXCollections.observableList(item?.grid?.toList() ?: listOf()))
    }

    val list2: ListProperty<List<Day13.TileAtPoint>> = bind {
        SimpleListProperty(FXCollections.observableList(item?.rows() ?: listOf()))
    }

    val score: LongProperty = bind {
        SimpleLongProperty(item?.score ?: 0)
    }
}

class GameView : View() {
    private val gameStateView: GameStateViewModel by inject()

    override val root = flowpane {
        bindChildren(gameStateView.list2) { rows ->
            println("Rows ${rows.size}")
            hbox {
                rows.forEach { (tile, point) ->
                    val (x, y) = point
                    val colour = when (tile) {
                        Tile.Empty -> Color.TRANSPARENT
                        Tile.Wall -> Color.BLACK
                        Tile.Block -> Color.BROWN
                        Tile.Paddle -> Color.BLUE
                        Tile.Ball -> Color.CHOCOLATE
                    }
                    rectangle(
                            x = size * x,
                            y = size * y,
                            width = size,
                            height = size
                    ) {
                        fill = colour
                    }
                }
            }

        }
    }

    init {
        subscribe<GameStateEvent> { event ->
            println("Getting new event")
            gameStateView.list2.set(FXCollections.observableList(event.gameState.rows()))
        }
    }
}

class Da13View : View() {

    private val gameView: GameView by inject()

    private val scoreProperty = SimpleStringProperty()

    override val root = borderpane {
        top = hbox {
            button("start game").action {
                fire(StartGameEvent)
            }
            label {
                bind(scoreProperty)
            }
        }
        bottom = gameView.root
    }

    init {
        println("Initialising View")

        subscribe<StartGameEvent> {
            println("StartGameEvent received")
            Day13.run {
                runGame(intCode) {
                    println("sending new game state")
                    fire(GameStateEvent(it))
                    Thread.sleep(5)
                }
            }
        }

        subscribe<GameStateEvent> { event ->
            println("Updating score")
            scoreProperty.set(event.gameState.score.toString())
        }
    }
}