package com.ctl.aoc.kotlin.y2022

import com.ctl.aoc.kotlin.utils.Queue
import com.ctl.aoc.kotlin.utils.traversal
import kotlin.math.ceil

private typealias ResourceCount = Map<Day19.Resource, Int>
private typealias MutableResourceCount = MutableMap<Day19.Resource, Int>

object Day19 {

    enum class Resource {
        Ore, Clay, Obsidian, Geode
    }

    data class BluePrint(
        val id: Int,
        val robotCost: Map<Resource, ResourceCount>
    ) {

        /**
         * For each of the resources, we get the highest cost across all the robots
         */
        val maxNeeded: ResourceCount by lazy {
            robotCost.values
                .asSequence()
                .flatMap { it.asSequence() }
                .groupBy { it.key }
                .mapValues { entry -> entry.value.maxOf { it.value } }
        }
    }


    data class State(
        val bluePrint: BluePrint,
        val resources: ResourceCount,
        val robots: ResourceCount,
        val clock: Int = 24
    )

    /**
     * Return the missing resources to be able to build the following robot recipe, empty if nothing is missing
     */
    private fun ResourceCount.resourcesNeeded(buildCost: ResourceCount): ResourceCount {
        val missing: MutableResourceCount = mutableMapOf()
        buildCost.forEach { (resource, quantityNeeded) ->
            val needed = quantityNeeded - (this[resource] ?: 0)
            if (needed > 0) {
                missing[resource] = needed
            }
        }
        return missing
    }

    private fun MutableResourceCount.build(buildCost: ResourceCount) {
        buildCost.forEach { (resource, count) ->
            this.compute(resource) { _, current ->
                val newCount = current!! - count
                newCount
            }
        }
    }

    private fun MutableResourceCount.produce(robots: ResourceCount, cycles: Int = 1) {
        if (cycles > 0) {
            robots.forEach { (resource, count) ->
                this.compute(resource) { _, current -> (current ?: 0) + cycles * count }
            }
        }
    }

    private fun State.isRobotNeeded(resource: Resource): Boolean {
        if (resource == Resource.Geode) {
            return true
        }
        if ((robots[resource] ?: 0) == bluePrint.maxNeeded[resource]!!) {
            return false
        }

        if ((resources[resource] ?: 0) >= bluePrint.maxNeeded[resource]!! + 3) {
            return false
        }

        if (resource == Resource.Ore && clock <= 5) {
            return false
        }
        if (resource == Resource.Clay && clock <= 5) {
            return false
        }
        if (resource == Resource.Obsidian && clock <= 1) {
            return false
        }
        return true
    }

    private fun State.canMakeRobot(robot: Resource): Boolean {
        val cost = bluePrint.robotCost[robot]!!
        return resources.resourcesNeeded(cost).isEmpty()
    }

    private fun State.nextBuildingRobot(robot: Resource): State {
        val newRobots = robots.toMutableMap()
        newRobots.compute(robot) { _, count -> (count ?: 0) + 1 }

        val newResources = this.resources.toMutableMap()
        newResources.build(bluePrint.robotCost[robot]!!)
        newResources.produce(robots)

        return copy(
            resources = newResources,
            robots = newRobots,
            clock = clock - 1
        )
    }

    private fun State.nextStates(): Sequence<State> {
        if (this.clock == 0) {
            return sequenceOf()
        }
        return sequence {
            //if we can make a geode make it without exploring more states
            if (canMakeRobot(Resource.Geode)) {
                yield(nextBuildingRobot(Resource.Geode))
            }//if we can make an Obsidian make it without exploring more states
            else if (canMakeRobot(Resource.Obsidian)) {
                yield(nextBuildingRobot(Resource.Obsidian))
            } else {
                //We try to build each robot, jumping in time to when the given robot will be ready to build
                var atLeastOnce = false
                Resource.values().forEach { toBuild ->
                    if (isRobotNeeded(toBuild)) {
                        //if the robot is needed, we compute the resources missing and then jump in time
                        val cost = bluePrint.robotCost[toBuild]!!
                        val resourceNeeded = resources.resourcesNeeded(cost)
                        //we only try if we have at least one robot for all the needed resources
                        if (resourceNeeded.all { robots.containsKey(it.key) }) {
                            //we look at production rate for all resources, we find the critical path
                            val timeNeeded = resourceNeeded.mapValues { (r, quantityNeeded) ->
                                ceil(quantityNeeded.toDouble() / robots[r]!!).toInt()
                            }.maxByOrNull { it.value }?.value ?: 0
                            //if we wait for timeNeeded we are guaranteed to have enough to build this robot
                            if (timeNeeded < clock) {
                                val newRobots = robots.toMutableMap()
                                newRobots.compute(toBuild) { _, count -> (count ?: 0) + 1 }

                                val newResources = resources.toMutableMap()
                                //we produce while we wait
                                newResources.produce(robots, timeNeeded)
                                //we finally build the robot
                                newResources.build(cost)
                                //one last production before the robot is ready
                                newResources.produce(robots)
                                yield(
                                    copy(
                                        resources = newResources,
                                        robots = newRobots,
                                        clock = clock - timeNeeded - 1
                                    )
                                )
                                atLeastOnce = true
                            }
                        }
                    }
                }
                //if we didn't build anything because there wasn't enough time we just produce and advance the clock
                if (!atLeastOnce) {
                    val newResources = resources.toMutableMap()
                    newResources.produce(robots)
                    yield(
                        copy(
                            resources = newResources,
                            clock = clock - 1
                        )
                    )
                }
            }
        }
    }

    private fun BluePrint.bfs(startClock: Int): State {
        println("Doing blueprint ${this.id}")
        val start = State(
            bluePrint = this,
            resources = mapOf(),
            robots = mapOf(Resource.Ore to 1),
            clock = startClock
        )
        return traversal(
            startNode = start,
            storage = Queue(),
            index = { "${it.clock}-${it.resources}-${it.robots}" },
            nodeGenerator = { it.nextStates() }
        )
            .filter { it.clock == 0 }
            .maxBy { it.resources[Resource.Geode] ?: 0 }
    }

    fun solve1(input: Sequence<String>): Int {
        return input
            .map { it.toBluePrint() }
            .map { it to it.bfs(24) }
            .map { (bluePrint, maxState) ->
                val geodes = maxState.resources[Resource.Geode] ?: 0
                println("Blueprint ${bluePrint.id} can produce $geodes geodes (${maxState.resources}) with ${maxState.robots}")
                bluePrint.id * geodes
            }
            .sum()
    }

    fun solve2(input: Sequence<String>): Long {
        return input
            .take(3)
            .map { it.toBluePrint() }
            .map { it to it.bfs(32) }
            .map { (bluePrint, maxState) ->
                val geodes = maxState.resources[Resource.Geode] ?: 0
                println("Blueprint ${bluePrint.id} can produce $geodes geodes (${maxState.resources}) with ${maxState.robots}")
                geodes.toLong()
            }
            .fold(1) { acc, c -> acc * c }
    }

    private fun String.toBluePrint(): BluePrint = regex.matchEntire(this)
        ?.groupValues
        ?.drop(1)
        ?.map { it.toInt() }
        ?.let {
            val id = it[0]
            val robotCost = mapOf(
                Resource.Ore to mapOf(Resource.Ore to it[1]),
                Resource.Clay to mapOf(Resource.Ore to it[2]),
                Resource.Obsidian to mapOf(Resource.Ore to it[3], Resource.Clay to it[4]),
                Resource.Geode to mapOf(Resource.Ore to it[5], Resource.Obsidian to it[6]),
            )
            BluePrint(id = id, robotCost = robotCost)
        } ?: error(this)

    private val regex =
        """Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian.""".toRegex()

}
