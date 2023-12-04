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

    fun findNumberAt(input: List<String>, lineIndex: Int, charIndex: Int): Pair<Int, IntRange>? {
        if (lineIndex in input.indices && charIndex in input[lineIndex].indices) {
            val line = input[lineIndex]
            if (line[charIndex].isDigit()) {
                var left = charIndex
                while (left > 0 && line[left - 1].isDigit()) left--
                var right = charIndex
                while (right < line.length - 1 && line[right + 1].isDigit()) right++
                return Pair(lineIndex, IntRange(left, right))
            }
        }
        return null;
    }

    val adjecents = listOf(Pair(-1, 0), Pair(-1, 1), Pair(0, 1), Pair(1, 1), Pair(1, 0), Pair(1, -1), Pair(0, -1),
        Pair(-1, -1))
    fun part2(input: List<String>): Int {
        return input.mapIndexed { lineIndex, s ->
            s.mapIndexed { charIndex, c ->
                if (c == '*') {
                    adjecents.map {
                        findNumberAt(input, lineIndex + it.first, charIndex + it.second)
                    }.filterNotNull().distinct()
                } else {
                    listOf()
                }
            }.filter { it.size == 2 }.map {
                val first = it.first()
                val last = it.last()
                val firstNumber = input[first.first].substring(first.second).toInt()
                val secondNumber = input[last.first].substring(last.second).toInt()
                firstNumber * secondNumber
            }.sum()
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}