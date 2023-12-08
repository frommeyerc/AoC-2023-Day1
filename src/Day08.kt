import java.lang.IllegalArgumentException

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

    fun part2(input: List<String>): Int {
        val (network, instr) = parseInput(input)
        val start = network.keys.filter { it.endsWith('A') }
        var pos = start
        var pc = 0;
        var count = 0;
        while (pos.find { !it.endsWith('Z') } != null) {
            pos.forEachIndexed() { i, p ->
                if (p.endsWith('Z')) {
                    val paddedCount = count.toString().padStart(10, ' ')
                    println(paddedCount + " " + p.padStart((i + 1) * 4, ' '))
                }
            }
            count++
            pos = ghostGo(pos, network, instr[pc++])
            if (pc == instr.length) pc = 0
        }
        return count
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test2")
    //check(part2(testInput) == 6)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}