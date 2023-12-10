import java.awt.Dimension
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main() {

    fun toPipe(c: Char, from: Char): Triple<Char, Char, Int> {
        return when (c) {
            '|' -> if (from == 'N') Triple('N', 'S', 0) else Triple('S', 'N', 0)
            '-' -> if (from == 'W') Triple('W', 'E', 0) else Triple('E', 'W', 0)
            'L' -> if (from == 'N') Triple('N', 'E', -1) else Triple('E', 'N', 1)
            'J' -> if (from == 'N') Triple('N', 'W', 1) else Triple('W', 'N', -1)
            '7' -> if (from == 'S') Triple('S', 'W', -1) else Triple('W', 'S', 1)
            'F' -> if (from == 'S') Triple('S', 'E', 1) else Triple('E', 'S', -1)
            else -> Triple('X', 'X', 0)
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

    fun follow2(input: List<String>, x: Int, y: Int, field: Array<Array<Int>>): Int {
        val adjacent: List<Triple<Int, Int, Char>> = listOf(
            Triple(x, y-1, 'S'),
            Triple(x+1, y, 'W'),
            Triple(x, y+1, 'N'),
            Triple(x, y-1, 'E'))
        var next = adjacent.filter { it.first >= 0 && it.second >= 0 }
            .find { toPipe(input[it.second][it.first], it.third).first == it.third } ?: throw IllegalStateException()
        field[x * 3 + 1][y * 3 + 1] = 1
        if (next.third == 'S') field[x * 3 + 1][y * 3] = 1
        if (next.third == 'N') field[x * 3 + 1][y * 3 + 2] = 1
        if (next.third == 'E') field[x * 3][y * 3 + 1] = 1
        if (next.third == 'W') field[x * 3 + 2][y * 3 + 1] = 1
        var pipe = toPipe(input[next.second][next.first], next.third)
        var bendRight = 0
        while (pipe.first != 'X') {
            if (pipe.first == 'N' || pipe.second == 'N')
                field[next.first * 3 + 1][next.second * 3] = 1
            if (pipe.first == 'S' || pipe.second == 'S')
                field[next.first * 3 + 1][next.second * 3 + 2] = 1
            if (pipe.first == 'W' || pipe.second == 'W')
                field[next.first * 3][next.second * 3 + 1] = 1
            if (pipe.first == 'E' || pipe.second == 'E')
                field[next.first * 3 + 2][next.second * 3 + 1] = 1
            field[next.first * 3 + 1][next.second * 3 + 1] = 1
            bendRight += pipe.third
            next = when (pipe.second) {
                'N' -> Triple(next.first, next.second - 1, 'S')
                'E' -> Triple(next.first + 1, next.second, 'W')
                'S' -> Triple(next.first, next.second + 1, 'N')
                'W' -> Triple(next.first - 1, next.second, 'E')
                else -> throw IllegalStateException()
            }
            pipe = toPipe(input[next.second][next.first], next.third)
        }
        if (next.third == 'S') field[x * 3 + 1][y * 3 + 2] = 1
        if (next.third == 'N') field[x * 3 + 1][y * 3] = 1
        if (next.third == 'E') field[x * 3 + 2][y * 3 + 1] = 1
        if (next.third == 'W') field[x * 3][y * 3 + 1] = 1
        return bendRight
    }

    fun part1(input: List<String>): Int {
        val startOfAnimal = findAnimal(input)
        val length = follow(input, startOfAnimal.first, startOfAnimal.second)
        return length / 2
    }

    val dirs = listOf(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0))

    fun markNext(field: Array<Array<Int>>, pos: Pair<Int, Int>) {
        if (pos.first in field.indices && pos.second in 0..< field[0].size)
            if (field[pos.first][pos.second] == 0) {
                field[pos.first][pos.second] = 2
                dirs.forEach { markNext(field, Pair(pos.first + it.first, pos.second + it.second)) }
            }
    }

    fun intToRgb(value: Int): Int {
        return when (value) {
            1 -> 0x000000
            2 -> 0x0000FF
            else -> 0xFFFFFF
        }
    }

    fun markOutside(field: Array<Array<Int>>) {
        val start = Pair(0,0)
        val border = mutableListOf(start)
        while (border.isNotEmpty()) {
            val next = border.removeFirst()
            if (field[next.first][next.second] == 0) {
                field[next.first][next.second] = 2
                dirs.map { Pair(next.first + it.first, next.second + it.second) }
                    .filter { it.first in field.indices && it.second in field[0].indices }
                    .forEach { border.add(it) }
            }
        }
        val size = Dimension(field.size, field[0].size)
        val img = BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB)
        for (x in field.indices)
            for (y in field[x].indices) {
                img.setRGB(x, y, intToRgb(field[x][y]))
            }
        ImageIO.write(img, "BMP", File("test1.bmp"))
    }

    fun squareFree(field: Array<Array<Int>>, x: Int, y: Int): Boolean {
        for (i in 0..2)
            for (j in 0..2)
                if (field[x * 3 + i][y * 3 + j] != 0) return false
        return true
    }

    fun countInner(field: Array<Array<Int>>): Int {
        val xMax = field.size / 3
        val yMax = field[0].size / 3
        var count = 0
        for (x in 0..<xMax)
            for (y in 0..<yMax) {
                if (squareFree(field, x, y)) count++
            }
        return count
    }

    fun part2(input: List<String>): Int {
        val field = Array(input[0].length * 3) { Array<Int>(input.size * 3) {0} }
        val startOfAnimal = findAnimal(input)
        val rightBends = follow2(input, startOfAnimal.first, startOfAnimal.second, field)
        val size = Dimension(field.size, field[0].size)
        val img = BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB)
        for (x in field.indices)
            for (y in field[x].indices) {
                img.setRGB(x, y, intToRgb(field[x][y]))
            }
        ImageIO.write(img, "BMP", File("test.bmp"))
        markOutside(field)
        return countInner(field)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 8)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}