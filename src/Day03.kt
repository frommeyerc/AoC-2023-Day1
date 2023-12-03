import kotlin.math.max
import kotlin.math.min

fun main() {

    fun checkLine(line: String, range: IntRange): Boolean {
        val regex = """[^.0-9]""".toRegex()
        val startIndex = max(range.first - 1, 0)
        val endIndex = min(range.last + 2, line.length)
        val subSequence = line.subSequence(startIndex, endIndex)
        return regex.find(subSequence) != null
    }

    fun part1(input: List<String>): Int {
        val regex = """([0-9]+)""".toRegex()
        return input.mapIndexed { index, s ->
            regex.findAll(s).sumOf {
                if ((index > 0 && checkLine(input[index - 1], it.range))
                    || ((index < input.size - 1) && checkLine(input[index + 1], it.range))
                    || ((it.range.first > 0) && s[it.range.first - 1] != '.')
                    || ((it.range.last < s.length - 1) && s[it.range.last + 1] != '.'))
                    it.value.toInt()
                else
                    0
            }
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}