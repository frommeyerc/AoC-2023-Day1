import kotlin.math.max
import kotlin.math.min

fun main() {
    fun buildWorkflows(input: List<String>): Map<String, Workflow> {
        val regex = """(?:([xmas])([<>])([0-9]+):)?([a-zAR]+),?""".toRegex()
        return input.takeWhile { it.isNotBlank() }.map {
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
    }

    fun part1(input: List<String>): Int {
        val workflows = buildWorkflows(input)

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

    fun part2(input: List<String>): Long {
        val workflows = buildWorkflows(input)
        val paths = mutableListOf(Path("in", mapOf(
            Pair('x', Triple('x', 1, 4000)),
            Pair('m', Triple('m', 1, 4000)),
            Pair('a', Triple('a', 1, 4000)),
            Pair('s', Triple('s', 1, 4000)),
        )))
        var accepted = 0L
        while(paths.isNotEmpty()) {
            val path = paths.removeFirst()
            when (path.target) {
                "A" -> accepted += path.size()
                "R" -> continue
                else -> {
                    val wf = workflows[path.target]!!
                    var ranges = path.ranges
                    for (rule in wf.rules) {
                        paths.add(Path(rule.target, rule.applyRange(ranges, rule.range())))
                        ranges = rule.applyRange(ranges, rule.reverseRange())
                    }
                }
            }
        }
        return accepted
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
    val part1 = part1(testInput)
    if(part1 != 19114)
        throw IllegalStateException("Wrong test result: $part1")
    val part2 = part2(testInput)
    if(part2 != 167409079868000)
        throw IllegalStateException("Wrong test result: $part2")

    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}

abstract class WFRule(val variable: Char, val value: Int, val target: String) {
    abstract fun matches(part: Map<Char, Int>): Boolean

    abstract fun range(): Triple<Char, Int, Int>

    abstract fun reverseRange(): Triple<Char, Int, Int>

    open fun applyRange(ranges: Map<Char, Triple<Char, Int, Int>>, range: Triple<Char, Int, Int>): Map<Char, Triple<Char, Int, Int>> {
        return if (ranges[variable] == null) {
            ranges.plus(Pair(variable, range()))
        } else {
            ranges.mapValues {
                if (it.key == variable) {
                    Triple(variable, max(range.second, it.value.second), min(range.third, it.value.third))
                } else it.value
            }
        }
    }
}

class LtRule(variable: Char, value: Int, target: String) : WFRule(variable, value, target) {
    override fun matches(part: Map<Char, Int>): Boolean {
        return part[variable]!! < value
    }

    override fun range(): Triple<Char, Int, Int> {
        return Triple(variable, 1, value - 1)
    }

    override fun reverseRange(): Triple<Char, Int, Int> {
        return Triple(variable, value, 4000)
    }
}

class GtRule(variable: Char, value: Int, target: String) : WFRule(variable, value, target) {
    override fun matches(part: Map<Char, Int>): Boolean {
        return part[variable]!! > value
    }

    override fun range(): Triple<Char, Int, Int> {
        return Triple(variable, value + 1, 4000)
    }

    override fun reverseRange(): Triple<Char, Int, Int> {
        return Triple(variable, 1, value)
    }
}

class FinalRule(target: String) : WFRule('X', 0, target) {
    override fun matches(part: Map<Char, Int>): Boolean {
        return true
    }

    override fun range(): Triple<Char, Int, Int> {
        return Triple('X', 0, 0) // not used
    }

    override fun reverseRange(): Triple<Char, Int, Int> {
        return Triple('X', 0, 0)
    }

    override fun applyRange(ranges: Map<Char, Triple<Char, Int, Int>>, range: Triple<Char, Int, Int>): Map<Char, Triple<Char, Int, Int>> {
        return ranges
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

class Path(val target: String, val ranges: Map<Char, Triple<Char, Int, Int>>){
    fun size(): Long {
        var size = 1L
        ranges.forEach { (_, range) -> size *= (range.third - range.second + 1) }
        return size
    }

}