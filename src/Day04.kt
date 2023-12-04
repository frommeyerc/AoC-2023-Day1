fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            val split = it.split(':','|')
            val winning = split[1].trim().split(' ')
            val having = split[2].trim().split(' ')
            having.filter { it.isNotBlank() }.count {
                winning.contains(it)
            }.let {
                if (it > 0) 1 shl (it - 1) else 0
            }
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}