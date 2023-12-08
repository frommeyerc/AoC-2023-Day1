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

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 2)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}