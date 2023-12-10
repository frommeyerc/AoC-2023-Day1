fun main() {

    fun toPipe(c: Char, from: Char): Pair<Char, Char> {
        return when (c) {
            '|' -> if (from == 'N') Pair('N', 'S') else Pair('S', 'N')
            '-' -> if (from == 'W') Pair('W', 'E') else Pair('E', 'W')
            'L' -> if (from == 'N') Pair('N', 'E') else Pair('E', 'N')
            'J' -> if (from == 'N') Pair('N', 'W') else Pair('W', 'N')
            '7' -> if (from == 'S') Pair('S', 'W') else Pair('W', 'S')
            'F' -> if (from == 'S') Pair('S', 'E') else Pair('E', 'S')
            else -> Pair('X', 'X')
        }
    }

    fun findAnimal(input: List<String>): Pair<Int, Int> {
        val y = input.indexOfFirst { it.contains('S') }
        val x = input[y].indexOf('S')
        return Pair(x, y)
    }

    fun follow(input: List<String>, x: Int, y: Int): Int {
        val adjacent: List<Triple<Int, Int, Char>> = listOf(
            Triple(x, y-1, 'S'),
                Triple(x+1, y, 'W'),
                Triple(x, y+1, 'N'),
                Triple(x, y-1, 'E'))
        var next = adjacent.filter { it.first >= 0 && it.second >= 0 }
            .find { toPipe(input[it.second][it.first], it.third).first == it.third } ?: throw IllegalStateException()
        var pipe = toPipe(input[next.second][next.first], next.third)
        var count = 1
        while (pipe.first != 'X') {
            count++
            next = when (pipe.second) {
                'N' -> Triple(next.first, next.second - 1, 'S')
                'E' -> Triple(next.first + 1, next.second, 'W')
                'S' -> Triple(next.first, next.second + 1, 'N')
                'W' -> Triple(next.first - 1, next.second, 'E')
                else -> throw IllegalStateException()
            }
            pipe = toPipe(input[next.second][next.first], next.third)
        }
        return count
    }

    fun part1(input: List<String>): Int {
        val startOfAnimal = findAnimal(input)
        val length = follow(input, startOfAnimal.first, startOfAnimal.second)
        return length / 2
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 8)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}