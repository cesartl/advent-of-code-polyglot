package com.ctl.aoc.kotlin.utils

object BuildOrder {

    fun <T> buildOrder(nodes: Collection<T>, dependencies: List<Pair<T, T>>): List<T>
            where T : Comparable<T> {
        val g: Graph<T> = Graph()
        dependencies.forEach { g.addDirectedEdge(it.first, it.second) }

        var remaining = nodes
        val result = mutableListOf<T>()

        while (remaining.isNotEmpty()) {
            val (readyToBuild, other) = remaining.partition { g.incomingNodes(it).isEmpty() }
            val sorted = readyToBuild.sorted()
            if (sorted.isNotEmpty()) {
                result.add(sorted.first())
                sorted.take(1).forEach { g.removeAllOutgoing(it) }
            }
            if (other.size >= remaining.size) {
                throw IllegalAccessException("cyclical")
            }
            remaining = other + sorted.drop(1)
        }
        return result.toList()
    }

    data class Worker(val id: Int, val freeAt: Int)

    fun <T> buildOrder(nodes: Collection<T>, dependencies: List<Pair<T, T>>, nWorkers: Int, stepTime: (T) -> Int): List<Pair<T, Int>>
            where T : Comparable<T> {
        var workers = (0..(nWorkers - 1)).map { Worker(it, 0) }
        println("start workers")
        println(workers)
        val g: Graph<T> = Graph()

        dependencies.forEach { g.addDirectedEdge(it.first, it.second) }

        var remaining = nodes
        val result = mutableListOf<Pair<T, Int>>()
        var currentTime = 0
        var workInProgress = listOf<Pair<T, Int>>()
        while (remaining.isNotEmpty()) {
            println()
            println("current time $currentTime" )

            val (finished, remainingWork) = workInProgress.partition { it.second <= currentTime }
            finished.forEach { g.removeAllOutgoing(it.first) }
            workInProgress = remainingWork

            val (readyToBuild, other) = remaining.partition { g.incomingNodes(it).isEmpty() }
            val sorted = readyToBuild.sorted()

            if (sorted.isNotEmpty()) {

                val (freeWorkers, busyWorkers) = workers.partition { it.freeAt <= currentTime }

                println("freeWorkers $freeWorkers")
                println("sorted $sorted")

                val workLoad = freeWorkers.zip(sorted).map { (worker, node) -> worker.copy(freeAt = currentTime + stepTime(node)) to node }.sortedBy { it.first.freeAt }

                workInProgress += workLoad.map { it.second to it.first.freeAt }

                workLoad.forEach { result.add(it.second to it.first.freeAt) }



                result.sortBy { it.second }
                println("result $result")
                println("workload $workLoad")

                val workerAtWork = workLoad.map { it.first }.toList()
                val ids = workerAtWork.map { it.id }.toSet()

                workers = (workerAtWork.toList() + workers.filter { !ids.contains(it.id) }).sortedBy { it.freeAt }
                println("workers $workers")
                remaining = other + sorted.drop(workLoad.size)
            }

            currentTime = Math.max(workers.filter { it.freeAt > 0 }.first().freeAt, currentTime + 1)

//            if (other.size >= remaining.size) {
//                throw IllegalAccessException("cyclical")
//            }
        }

//        println(result.toList())

        return result.toList()

    }

}