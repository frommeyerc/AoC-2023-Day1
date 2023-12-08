import java.lang.IllegalArgumentException
import kotlin.math.max
import kotlin.math.pow

fun main() {

    fun parseInput(input: List<String>): Pair<Map<String, Pair<String, String>>, String> {
        val instr = input[0]
        val regex = """([A-Z]{3}) = [(]([A-Z]{3}), ([A-Z]{3})[)]""".toRegex()
        val network = input.drop(2).associate {
            val groups = regex.find(it)!!.groupValues
            Pair(groups[1], Pair(groups[2], groups[3]))
        }
        return Pair(network, instr)
    }

    fun part1(input: List<String>): Int {
        val (network, instr) = parseInput(input)
        var pos = "AAA"
        var count = 0
        var pc = 0;
        while (pos != "ZZZ") {
            count++
            pos = when (instr[pc++]) {
                'L' -> network[pos]!!.first
                'R' -> network[pos]!!.second
                else -> throw IllegalArgumentException("Got wrong direction")
            }
            if (pc == instr.length) pc = 0
        }
        return count
    }

    fun ghostGo(positions: List<String>, network: Map<String, Pair<String, String>>, dir: Char): List<String> {
        return positions.map {
            when (dir) {
                'L' -> network[it]!!.first
                'R' -> network[it]!!.second
                else -> throw IllegalArgumentException()
            }
        }
    }

    fun move(pos: String, network: Map<String, Pair<String, String>>, dir: Char): String {
        return when (dir) {
            'L' -> network[pos]!!.first
            'R' -> network[pos]!!.second
            else -> throw IllegalArgumentException()
        }
    }

    fun factorize(n: Int): List<Int> {
        var remaining = n
        var factor = 2
        val result = mutableListOf<Int>()
        while (factor * factor <= remaining) {
            while (remaining % factor == 0) {
                result.add(factor)
                remaining /= factor
            }
            factor++
        }
        result.add(remaining)
        return result
    }

    fun part2(input: List<String>): Long {
        val (network, instr) = parseInput(input)
        val start = network.keys.filter { it.endsWith('A') }
        return start.map {
            var p = it
            var pc = 0;
            var count = 0;
            while (!p.endsWith('Z')) {
                count++
                p = move(p, network, instr[pc++])
                if (pc == instr.length) pc = 0
            }
            count
        }.map { factorize(it) }.map { it.groupBy { it }.mapValues { it.value.size } }
            .fold(mapOf<Int, Int>()) { acc, factors ->
            listOf(acc.keys, factors.keys).flatten().associate {
                Pair(it, max(acc.getOrDefault(it, 0), factors.getOrDefault(it, 0)))
            }
        }.entries.fold(1L) { acc, e -> acc * e.key.toDouble().pow(e.value).toLong() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test2")
    //check(part2(testInput) == 6)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}