val DIGIT_WORDS = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

fun main() {
    fun part1(input: List<String>): Int {
        return input.map {
            val digits = it.filter { c -> c.isDigit() }
            when (digits.length) {
                0 -> 0
                1 -> (digits + digits).toInt()
                2 -> digits.toInt()
                else -> digits.removeRange(1, digits.length - 1).toInt()
            }
        }.sum()
    }

    fun wordToDigit(s: String): Int {
        return DIGIT_WORDS.indexOf(s) + 1
    }

    fun part2(input: List<String>): Int {
        return input.sumOf {
            var firstDigit = it.indexOfFirst { c -> c.isDigit() }
            if (firstDigit == -1) firstDigit = Int.MAX_VALUE
            val firstWord = it.findAnyOf(DIGIT_WORDS)
            firstDigit = if (firstWord != null && firstDigit > firstWord.first) {
                wordToDigit(firstWord.second)
            } else
                it[firstDigit].digitToInt()
            var secondDigit = it.indexOfLast { it.isDigit() }
            val lastWord = it.findLastAnyOf(DIGIT_WORDS)
            secondDigit = if (lastWord != null && secondDigit < lastWord.first) {
                wordToDigit(lastWord.second)
            } else
                it[secondDigit].digitToInt()
            (10 * firstDigit + secondDigit)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day01_test")
    check(part1(testInput1) == 142)
    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
