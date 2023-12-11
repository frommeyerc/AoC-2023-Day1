import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val emptyCols = mutableListOf<Int>()
        for (i in input[0].indices) {
            var j = 0
            while (j < input.size && input[j][i] == '.') j++
            if ( j == input.size) emptyCols.add(i)
        }
        var count = 0;
        var index = 0;
        val galaxies = input.map {
            var s = it
            emptyCols.reversed().forEach { index -> s = s.substring(0, index) + '.' + s.substring(index) }
            s
        }.map { row ->
            val galaxies = mutableListOf<Triple<Int, Int, Int>>()
            for (i in row.indices) {
                if (row[i] == '#') galaxies.add(Triple(i, index, count++))
            }
            index += if (galaxies.isEmpty()) 2 else 1
            galaxies
        }.flatten()
        var sum = 0
        for (i in galaxies.indices) {
            for (j in (i+1)..<galaxies.size) {
                sum += abs(galaxies[i].first - galaxies[j].first) + abs(galaxies[i].second - galaxies[j].second)
            }
        }
        return sum
    }

    fun part2(input: List<String>): Long {
        val emptyCols = mutableListOf<Int>()
        for (i in input[0].indices) {
            var j = 0
            while (j < input.size && input[j][i] == '.') j++
            if ( j == input.size) emptyCols.add(i)
        }
        var count = 0
        var y = 0L
        val galaxies = input.map {
            val galaxies = mutableListOf<Triple<Long, Long, Int>>()
            var x = 0L
            for (i in it.indices) {
                if (it[i] == '#') galaxies.add(Triple(x, y, count++))
                x += if (emptyCols.contains(i)) 1_000_000L else 1L
            }
            y += if (galaxies.isEmpty()) 1_000_000L else 1L
            galaxies
        }.flatten()
        var sum = 0L
        for (i in galaxies.indices) {
            for (j in (i+1)..<galaxies.size) {
                sum += abs(galaxies[i].first - galaxies[j].first) + abs(galaxies[i].second - galaxies[j].second)
            }
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}