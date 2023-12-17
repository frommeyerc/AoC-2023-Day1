import java.awt.Dimension
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main() {
    fun snapshot(input: List<String>, energized: Array<Array<Int>>) {
        val img = BufferedImage(energized.size * 3, energized[0].size * 3, BufferedImage.TYPE_INT_RGB)
        for (x in energized.indices)
            for (y in energized[x].indices) {
                for (i in 0..2)
                    for (j in 0..2)
                        img.setRGB(x * 3 + i, y * 3 + j, if (energized[x][y] > 0) 0x00ff00 else 0xffffff)
                when (input[y][x]) {
                    '\\' -> {
                        img.setRGB(x * 3, y * 3, 0)
                        img.setRGB(x * 3 + 1, y * 3 + 1, 0)
                        img.setRGB(x * 3 + 2, y * 3 + 2, 0)
                    }
                    '/' -> {
                        img.setRGB(x * 3 + 2, y * 3, 0)
                        img.setRGB(x * 3 + 1, y * 3 + 1, 0)
                        img.setRGB(x * 3, y * 3 + 2, 0)
                    }
                    '-' -> {
                        img.setRGB(x * 3, y * 3 + 1, 0)
                        img.setRGB(x * 3 + 1, y * 3 + 1, 0)
                        img.setRGB(x * 3 + 2, y * 3 + 1, 0)
                    }
                    '|' -> {
                        img.setRGB(x * 3 + 1, y * 3, 0)
                        img.setRGB(x * 3 + 1, y * 3 + 1, 0)
                        img.setRGB(x * 3 + 1, y * 3 + 2, 0)
                    }
                }
            }
        ImageIO.write(img, "JPG", File("test1.jpg"))

    }

    fun calcEnergizing(input: List<String>, start: Triple<Int, Int, Direction>): Int {
        val energized = Array(input[0].length) { Array(input.size) { 0 } }
        val beams = mutableListOf(start)
        while (beams.isNotEmpty()) {
            val beam = beams.removeFirst()
            var next = beam.third.nextField(beam.first, beam.second)
            var dir = beam.third
            while(next.first in input[0].indices && next.second in input.indices) {
                val eLevel = energized[next.first][next.second]
                if (eLevel and (dir.bitValue) > 0) break
                energized[next.first][next.second] = eLevel or (dir.bitValue)
                val newDir = dir.changeDir(input[next.second][next.first])
                if (newDir.size == 2)
                    beams.add(Triple(next.first, next.second, newDir[1]))
                dir = newDir[0]
                next = dir.nextField(next.first, next.second)
            }
        }
        return energized.sumOf { it.count { it > 0 } }
    }

    fun part1(input: List<String>): Int {
        return calcEnergizing(input, Triple(-1, 0, Direction.RIGHT))
    }

    fun part2(input: List<String>): Int {
        return maxOf(
            input.indices.maxOf { calcEnergizing(input, Triple(-1, it, Direction.RIGHT)) },
            input.indices.maxOf { calcEnergizing(input, Triple(input[0].length, it, Direction.LEFT)) },
            input[0].indices.maxOf { calcEnergizing(input, Triple(-1, it, Direction.DOWN)) },
            input[0].indices.maxOf { calcEnergizing(input, Triple(input.size, it, Direction.UP)) }
        )
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 46)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}

enum class Direction(private val x: Int, private val y: Int, val bitValue: Int) {
    LEFT(-1, 0, 1), RIGHT(1, 0, 2), UP(0, -1, 4), DOWN(0, 1, 8);

    fun changeDir(c: Char): List<Direction> {
        return when (c) {
            '.' -> listOf(this)
            '/' -> listOf(fromDir(if (x == 0) y * -1 else 0, if (y == 0) x * -1 else 0))
            '\\' -> listOf(fromDir(if (x == 0) y else 0, if (y == 0) x else 0))
            '-' -> if (x != 0) listOf(this) else listOf(LEFT, RIGHT)
            else -> if (x != 0) listOf(UP, DOWN) else listOf(this)
        }
    }

    fun nextField(x0: Int, y0:Int): Pair<Int, Int> {
        return Pair(x0 + x, y0 + y)
    }

    companion object {
        fun fromDir(x: Int, y: Int): Direction {
            return when {
                x == 0 && y == 1 -> Direction.DOWN
                x == 0 && y == -1 -> Direction.UP
                x == 1 && y == 0 -> Direction.RIGHT
                else -> Direction.LEFT
            }
        }
    }
}
