fun main() {
    fun part1(input: List<String>): Int {
        val regex = """(?:([xmas])([<>])([0-9]+):)?([a-zAR]+),?""".toRegex()
        val workflows = input.takeWhile { it.isNotBlank() }.map {
            val matches = sequence {
                var matcher = regex.find(it)
                while(matcher != null) {
                    yield(matcher.groupValues)
                    matcher = matcher.next()
                }
            }.toList()

            Workflow(matches.first()[4], matches.drop(1).map {
                when (it[2]) {
                    "<" -> LtRule(it[1][0], it[3].toInt(), it[4])
                    ">" -> GtRule(it[1][0], it[3].toInt(), it[4])
                    else -> FinalRule(it[4])
                }
            })
        }.associateBy { it.name }

        return input.takeLastWhile { it.isNotBlank() }.mapNotNull {
            val valuePairs = it.substring(1, it.lastIndex).split(',')
            val part = valuePairs.map { it.split('=') }.associate { Pair(it[0][0], it[1].toInt()) }
            var wfName = "in"
            while (!listOf("A", "R").contains(wfName)) {
                wfName = workflows[wfName]!!.run(part)
            }
            if (wfName == "A") part else null
        }.sumOf {
            it.map { it.value }.sum()
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
    val part1 = part1(testInput)
    if(part1 != 19114)
        throw IllegalStateException("Wrong test result: $part1")

    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}

abstract class WFRule(val variable: Char, val value: Int, val target: String) {
    abstract fun matches(part: Map<Char, Int>): Boolean
}

class LtRule(variable: Char, value: Int, target: String) : WFRule(variable, value, target) {
    override fun matches(part: Map<Char, Int>): Boolean {
        return part[variable]!! < value
    }
}

class GtRule(variable: Char, value: Int, target: String) : WFRule(variable, value, target) {
    override fun matches(part: Map<Char, Int>): Boolean {
        return part[variable]!! > value
    }
}

class FinalRule(target: String) : WFRule('X', 0, target) {
    override fun matches(part: Map<Char, Int>): Boolean {
        return true
    }
}

class Workflow(val name: String, val rules: List<WFRule>) {
    fun run(part: Map<Char, Int>): String {
        for (rule in rules) {
            if (rule.matches(part))
                return rule.target
        }
        throw IllegalStateException()
    }
}