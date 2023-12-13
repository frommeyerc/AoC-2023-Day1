fun main() {
    fun part1(input: List<String>): Int {
        val patterns = mutableListOf<List<String>>()
        run {
            var i = 0;
            while (i < input.size) {
                val pattern = mutableListOf<String>()
                while (i < input.size && input[i].isNotBlank()) pattern.add(input[i++])
                i++
                patterns.add(pattern)
            }
        }
        return patterns.map {
            //vertical mirror?
            cols@for (i in 2..<it[0].length step 2) {
                for (line in it) {
                    val reversed = line.reversed()
                    if (line.take(i) != reversed.takeLast(i)) continue@cols;
                }
                return@map i / 2
            }
            cols@for (i in 2..<it[0].length step 2) {
                for (line in it) {
                    val reversed = line.reversed()
                    if (line.takeLast(i) != reversed.take(i)) continue@cols
                }
                return@map it[0].length - (i/2)
            }
            //horizontal mirror?
            rows@for (i in 1..((it.size * 2) - 3) step 2) {
                for (j in 0..<(i+1)/2) {
                    if (i-j < it.size && it[j] != it[i-j]) continue@rows
                }
                return@map 100 * ((i+1)/2)
            }
            throw IllegalStateException()
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 405)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}