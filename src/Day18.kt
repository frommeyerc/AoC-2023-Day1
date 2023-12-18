import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val field = Array(1024) { Array(1024) {0} }
        var pos = Pair(300,300)
        input.map {
            it.split(' ').dropLast(1).zipWithNext { a, b -> Pair(a[0], b.toInt()) }.first()
        }.forEachIndexed { index, it ->
            pos = when (it.first) {
                'R' -> {
                    IntRange(pos.first, pos.first + it.second).forEach { field[it][pos.second] = field[it][pos.second] or 1 }
                    Pair(pos.first + it.second, pos.second)
                }
                'D' -> {
                    IntRange(pos.second, pos.second + it.second).forEach { field[pos.first][it] = field[pos.first][it] or 2 }
                    Pair(pos.first, pos.second + it.second)
                }
                'L' -> {
                    IntRange(pos.first - it.second, pos.first).forEach { field[it][pos.second] = field[it][pos.second] or 4 }
                    Pair(pos.first - it.second, pos.second)
                }
                'U' -> {
                    IntRange(pos.second - it.second, pos.second).forEach { field[pos.first][it] = field[pos.first][it] or 8 }
                    Pair(pos.first, pos.second - it.second)
                }
                else -> throw IllegalArgumentException()
            }
        }
        for (i in field.indices) {
            var count = 0
            var current = 0
            for (j in field[i].indices) {
                val horiz = field[i][j] and 5
                if (horiz > 0 && horiz != current) {
                    count++
                    current = horiz
                }
                if (field[i][j] == 0 && count % 2 == 1) field[i][j] = 16
            }
        }
        return field.sumOf { it.count { it > 0 } }
    }

    fun part2(input: List<String>): Long {
        var pos = Pair(0L, 0L)
        var boundaryPoints = 0
        val nodes = input.map { it.split(' ').lastOrNull()!! }
            .map { Pair(it.substring(2, 7).toInt(16), it[7]) }
            .map {
                pos = when (it.second) {
                    '0' -> Pair(pos.first + it.first, pos.second)
                    '1' -> Pair(pos.first, pos.second + it.first)
                    '2' -> Pair(pos.first - it.first, pos.second)
                    '3' -> Pair(pos.first, pos.second - it.first)
                    else -> throw IllegalArgumentException()
                }
                boundaryPoints += it.first
                pos
            }
        //Shoolace
        val coords1 = nodes.flatMap { it.toList().reversed() }
        val y0 = coords1.first()
        val term1 = coords1.drop(1).plus(y0).zipWithNext { xi, yj -> xi * yj }.sum()
        val coords2 = nodes.flatMap { it.toList() }
        val x0 = coords2.first()
        val term2 = coords2.drop(1).plus(x0).zipWithNext { yi, xj -> yi * xj }.sum()
        val a = abs(term1 - term2) / 2

        //Pick's
        val i = a - (boundaryPoints /2) + 1
        return (i + boundaryPoints).toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput) == 62)
    check(part2(testInput) == 952408144115L)

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}