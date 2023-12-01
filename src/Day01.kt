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

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
