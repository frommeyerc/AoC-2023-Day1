import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

fun main() {
    fun borderSpeed(raceTime: Long, record: Long): Pair<Double, Double> {
        val pBy2 = raceTime.toDouble() / 2
        val root = sqrt(pBy2 * pBy2 - record)
        return Pair(pBy2 - root, pBy2 + root)
    }

    fun part1(input: List<String>): Long {
        val time = input[0].split(' ').drop(1).filter { it.isNotBlank() }.map { it.toLong() }
        val distance = input[1].split(' ').drop(1).filter { it.isNotBlank() }.map { it.toLong() }
        return time.zip(distance)
            .map { borderSpeed(it.first, it.second) }
            .map { Pair(floor(it.first).toLong() + 1, ceil(it.second).toLong() - 1) }
            .map { it.second - it.first + 1 }.reduce { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Long {
        val time = input[0].filter { it != ' ' }.removePrefix("Time:").toLong()
        val distance = input[1].filter { it != ' ' }.removePrefix("Distance:").toLong()
        return borderSpeed(time, distance).let { Pair(floor(it.first).toLong() + 1, ceil(it.second).toLong() - 1) }
            .let { it.second - it.first + 1 }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288L)
    check(part2(testInput) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}