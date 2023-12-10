fun main() {

    fun work(numbers: List<Long>): Long {
        val lists = mutableListOf(numbers)
        var curList = numbers
        while (curList.find { it != 0L } != null) {
            curList = curList.zipWithNext { a, b -> b - a }
            lists.add(curList)
        }
        return lists.reversed().map { it.last() }.fold(0L) { acc, v -> acc + v }
    }

    fun part1(input: List<String>): Long {
        return input.asSequence().map { it.split(' ').map { it.toLong() } }.map { work(it) }.sum();
    }

    fun work2(numbers: List<Long>): Long {
        val lists = mutableListOf(numbers)
        var curList = numbers
        while (curList.find { it != 0L } != null) {
            curList = curList.zipWithNext { a, b -> b - a }
            lists.add(curList)
        }
        return lists.reversed().map { it.first() }.fold(0L) { acc, v -> v - acc }
    }

    fun part2(input: List<String>): Long {
        return input.asSequence().map { it.split(' ').map { it.toLong() } }.map { work2(it) }.sum();
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114L)
    check(part2(testInput) == 2L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}