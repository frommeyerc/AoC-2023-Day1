import kotlin.math.max

fun main() {
    fun checkPossible(game: Game): Boolean {
        game.draws.forEach {
            if (it.blue > 14 || it.green > 13 || it.red > 12) return false
        }
        return true
    }

    fun parseInput(input: List<String>): List<Game> {
        val regex = """Game ([0-9]+): |([0-9]+) blue|([0-9]+) green|([0-9]+) red|(;)""".toRegex()
        return input.map {
            var pos = 0
            var match = regex.find(it, pos)
            val game = Game(match!!.groupValues[1].toInt())
            while (pos < it.length) {
                pos = match!!.range.last + 1
                val draw = Draw()
                game.draws.add(draw)
                match = regex.find(it, pos)
                while (pos < it.length && match!!.groups[5] == null) {
                    if (match.groups[2] != null) draw.blue = draw.blue + match.groupValues[2].toInt()
                    if (match.groups[3] != null) draw.green = draw.green + match.groupValues[3].toInt()
                    if (match.groups[4] != null) draw.red = draw.red + match.groupValues[4].toInt()
                    pos = match.range.last + 1
                    match = regex.find(it, pos)
                }
            }
            game
        }
    }

    fun part1(input: List<String>): Int {
        return parseInput(input).sumOf { if (checkPossible(it)) it.number else 0 }
    }

    fun checkSmallAmount(game: Game): Int {
        return game.draws.map {
            Triple(it.blue, it.green, it.red)
        }.reduce { acc, triple ->
            Triple(max(acc.first, triple.first), max(acc.second, triple.second), max(acc.third, triple.third))
        }.let { it.first * it.second * it.third }
    }

    fun part2(input: List<String>): Int {
        return parseInput(input).sumOf { checkSmallAmount(it) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

class Game(val number: Int) {
    val draws: MutableList<Draw> = mutableListOf()
}

class Draw() {
    var green: Int = 0;
    var blue: Int = 0;
    var red: Int = 0;
}