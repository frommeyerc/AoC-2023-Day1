import kotlin.math.abs
import kotlin.math.min

fun main() {

    fun weightFor(input: List<String>, x: Int, y: Int, dir: Directions, target: Triple<Int, Int, Directions>): Int {
        var px = x
        var py = y
        var weight = 0
        while (abs(target.first - px) + abs(target.second - py) > 1) {
            px += dir.x
            py += dir.y
            weight += input[py][px].toString().toInt()
        }
        weight += input[target.second][target.first].toString().toInt()
        return weight
    }

    fun range(first: Int, second: Int): IntRange {
        return if (first <= second) IntRange(first, second) else IntRange(second, first)
    }

    fun targetsForDirection(x: Int, y: Int, dir: Directions): List<Triple<Int, Int, Directions>> {
        return range(x + 3 * dir.x, x + 9 * dir.x).flatMap { x1 ->
            range(y + 3 * dir.y, y + 9 * dir.y).flatMap { y1 ->
                when (dir) {
                    Directions.NORTH, Directions.SOUTH -> listOf(Directions.EAST, Directions.WEST)
                    Directions.EAST, Directions.WEST -> listOf(Directions.NORTH, Directions.SOUTH)
                }.map { Triple(x1, y1, it) }
            }
        }.map {
            Triple(it.first + it.third.x, it.second + it.third.y, it.third)
        }
    }

    fun makeUltraGraph(input: List<String>): Map<Triple<Int, Int, Directions>, Node> {
        val nodes = mutableMapOf<Triple<Int, Int, Directions>, Node>()
        for (x in input[0].indices)
            for (y in input.indices) {
                val coords = Pair(x, y)
                for (dir in Directions.entries) {
                    nodes[Triple(x, y, dir)] = Node(coords, dir)
                }
            }
        for (x in input[0].indices)
            for (y in input.indices) {
                for (dir in Directions.entries) {
                    nodes[Triple(x, y, dir)]!!.edges = targetsForDirection(x, y, dir)
                        .filter {
                            it.first in input[0].indices && it.second in input.indices
                                    && !(it.first == input[0].lastIndex && it.second == input.lastIndex)
                        }
                        .map {
                            val weight = weightFor(input, x, y, dir, it)
                            Pair(nodes[it]!!, weight)
                        }
                }
            }
        var finalNode = nodes[Triple(input[0].lastIndex, input.lastIndex, Directions.NORTH)]!!
        var w = input[input.lastIndex].last().toString().toInt() + input[input.lastIndex - 1].last().toString().toInt()
        for (i in 3..9) {
            val n = nodes[Triple(input[0].lastIndex, input.lastIndex - i, Directions.NORTH)]!!
            w += input[input.size - i].last().toString().toInt()
            n.edges += Pair(finalNode, w)
        }
        finalNode = nodes[Triple(input[0].lastIndex, input.lastIndex, Directions.WEST)]!!
        w = input.last()[input[0].lastIndex].toString().toInt() + input.last()[input[0].lastIndex - 1].toString().toInt()
        for (i in 3..9) {
            val n = nodes[Triple(input[0].lastIndex - i, input.lastIndex, Directions.WEST)]!!
            w += input.last()[input[0].length - i].toString().toInt()
            n.edges += Pair(finalNode, w)
        }

        w = IntRange(1, 10).sumOf { input[it][0].toString().toInt() } + input[10][1].toString().toInt()
        nodes[Triple(0,0, Directions.NORTH)]!!.edges += Pair(nodes[Triple(1, 10, Directions.WEST)]!!, w)
        w = IntRange(1, 10).sumOf { input[0][it].toString().toInt() } + input[1][10].toString().toInt()
        nodes[Triple(0,0, Directions.WEST)]!!.edges += Pair(nodes[Triple(10, 1, Directions.NORTH)]!!, w)

        return nodes
    }

    fun makeGraph(input: List<String>): Map<Triple<Int, Int, Directions>, Node> {
        val nodes = mutableMapOf<Triple<Int, Int, Directions>, Node>()
        for (x in input[0].indices)
            for (y in input.indices) {
                val coords = Pair(x, y)
                for (dir in Directions.entries) {
                    nodes[Triple(x, y, dir)] = Node(coords, dir)
                }
            }
        for (x in input[0].indices)
            for (y in input.indices) {
                for (dir in Directions.entries) {
                    val targets = when (dir) {
                        Directions.NORTH -> listOf(
                            Triple(x - 1, y, Directions.EAST),
                            Triple(x + 1, y, Directions.WEST),
                            Triple(x - 1, y + 1, Directions.EAST),
                            Triple(x + 1, y + 1, Directions.WEST),
                            Triple(x - 1, y + 2, Directions.EAST),
                            Triple(x + 1, y + 2, Directions.WEST),
                        )

                        Directions.SOUTH -> listOf(
                            Triple(x - 1, y, Directions.EAST),
                            Triple(x + 1, y, Directions.WEST),
                            Triple(x - 1, y - 1, Directions.EAST),
                            Triple(x + 1, y - 1, Directions.WEST),
                            Triple(x - 1, y - 2, Directions.EAST),
                            Triple(x + 1, y - 2, Directions.WEST),
                        )

                        Directions.WEST -> listOf(
                            Triple(x, y - 1, Directions.SOUTH),
                            Triple(x, y + 1, Directions.NORTH),
                            Triple(x + 1, y - 1, Directions.SOUTH),
                            Triple(x + 1, y + 1, Directions.NORTH),
                            Triple(x + 2, y - 1, Directions.SOUTH),
                            Triple(x + 2, y + 1, Directions.NORTH),
                        )

                        Directions.EAST -> listOf(
                            Triple(x, y - 1, Directions.SOUTH),
                            Triple(x, y + 1, Directions.NORTH),
                            Triple(x - 1, y - 1, Directions.SOUTH),
                            Triple(x - 1, y + 1, Directions.NORTH),
                            Triple(x - 2, y - 1, Directions.SOUTH),
                            Triple(x - 2, y + 1, Directions.NORTH),
                        )
                    }.filter { it.first in input[0].indices && it.second in input.indices }.map {
                        val weight = weightFor(input, x, y, dir, it)
                        Pair(nodes[it]!!, weight)
                    }
                    nodes[Triple(x, y, dir)]!!.edges = targets
                }
            }
        val finalNode = nodes[Triple(input[0].length - 1, input.size - 1, Directions.NORTH)]!!
        var w = 0
        for (i in 1..2) {
            val n = nodes[Triple(input[0].length - 1, input.size - 1 - i, Directions.NORTH)]!!
            w += input[input.size - i].last().toString().toInt()
            n.edges += Pair(finalNode, w)
        }
        w = 0
        for (i in 1..2) {
            val n = nodes[Triple(input[0].length - 1 - i, input.size - 1, Directions.WEST)]!!
            w += input.last()[input[0].length - i].toString().toInt()
            n.edges += Pair(finalNode, w)
        }

        return nodes
    }

    fun part1(input: List<String>): Int {
        val nodes = makeGraph(input)
        val boundary = mutableListOf(nodes[Triple(0,0,Directions.NORTH)]!!, nodes[Triple(0,0, Directions.WEST)]!!)
        boundary.forEach { it.heatLoss = 0 }
        while (boundary.isNotEmpty()) {
            boundary.sortBy { it.heatLoss }
            val next = boundary.removeFirst()
            next.edges.forEach {
                if (it.first.heatLoss > next.heatLoss + it.second) {
                    it.first.heatLoss = next.heatLoss + it.second
                    it.first.backtrack = next
                    if (!boundary.contains(it.first)) boundary.add(it.first)
                }
            }
        }
        return min (nodes[Triple(input[0].lastIndex, input.lastIndex, Directions.NORTH)]!!.heatLoss,
                nodes[Triple(input[0].lastIndex, input.lastIndex, Directions.WEST)]!!.heatLoss).toInt()
    }

    fun part2(input: List<String>): Long {
        val nodes = makeUltraGraph(input)
        val boundary = mutableListOf(nodes[Triple(0,0,Directions.NORTH)]!!, nodes[Triple(0,0, Directions.WEST)]!!)
        boundary.forEach { it.heatLoss = 0 }
        while (boundary.isNotEmpty()) {
            boundary.sortBy { it.heatLoss }
            val next = boundary.removeFirst()
            next.edges.forEach {
                if (it.first.heatLoss > next.heatLoss + it.second) {
                    it.first.heatLoss = next.heatLoss + it.second
                    it.first.backtrack = next
                    if (!boundary.contains(it.first)) boundary.add(it.first)
                }
            }
        }
        var n = nodes[Triple(input[0].lastIndex, input.lastIndex, Directions.NORTH)]
        val l = mutableListOf<Node>()
        while (n != null) {
            l.add(n)
            n = n.backtrack
        }
        l.reversed().forEach {
            println(it)
        }
        return min (nodes[Triple(input[0].lastIndex, input.lastIndex, Directions.NORTH)]!!.heatLoss,
            nodes[Triple(input[0].lastIndex, input.lastIndex, Directions.WEST)]!!.heatLoss)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput) == 102)
    check(part2(testInput) == 94L)

    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
}

enum class Directions(val x: Int, val y: Int) {
    NORTH(0, 1), SOUTH(0, -1), EAST(-1, 0), WEST(1, 0)
}

class Node(val coords: Pair<Int, Int>, val inDir: Directions) {
    var edges: List<Pair<Node, Int>> = listOf()
    var heatLoss: Long = Long.MAX_VALUE
    var backtrack: Node? = null

    override fun toString(): String {
        return "Node[$coords, $inDir, heatloss=$heatLoss]"
    }
}