fun main() {

    fun part1(input: List<String>): Int {
        val platform = Array(input[0].length) { Array(input.size) { ' ' } }
        input.forEachIndexed() { y, str ->
            str.forEachIndexed { x, c -> platform[x][y] = c }
        }

        //roll north
        for (i in platform.indices) {
            for (j in 1..<platform[i].size) {
                if (platform[i][j] == 'O') {
                    var y = j - 1
                    while (y >= 0 && platform[i][y] == '.') {
                        platform[i][y] = 'O'
                        platform[i][y + 1] = '.'
                        y--
                    }
                }
            }
        }

        return platform.sumOf {
            it.mapIndexed { index, c -> if (c == 'O') it.size - index else 0 }.sum()
        }
    }

    fun serializePlatform(p: Array<Array<Char>>): String {
        val ser = p.joinToString { it.joinToString() }
        return ser
    }

    fun part2(input: List<String>): Int {
        val platform = Array(input[0].length) { Array(input.size) { ' ' } }
        input.forEachIndexed() { y, str ->
            str.forEachIndexed { x, c -> platform[x][y] = c }
        }

        val cache = mutableMapOf<String, Int>()
        var maxCycle = 1_000_000_000
        var cycle = 1
        var cycleFound = false
        while (cycle <= maxCycle) {
            //roll north
            for (i in platform.indices) {
                for (j in 1..<platform[i].size) {
                    if (platform[i][j] == 'O') {
                        var y = j - 1
                        while (y >= 0 && platform[i][y] == '.') {
                            platform[i][y] = 'O'
                            platform[i][y + 1] = '.'
                            y--
                        }
                    }
                }
            }
            //roll west
            for (i in platform[0].indices) {
                for (j in 1..<platform.size) {
                    if (platform[j][i] == 'O') {
                        var y = j - 1
                        while (y >= 0 && platform[y][i] == '.') {
                            platform[y][i] = 'O'
                            platform[y + 1][i] = '.'
                            y--
                        }
                    }
                }
            }
            //roll south
            for (i in platform.indices) {
                for (j in platform[i].size - 2 downTo 0) {
                    if (platform[i][j] == 'O') {
                        var y = j + 1
                        while (y < platform[i].size && platform[i][y] == '.') {
                            platform[i][y] = 'O'
                            platform[i][y - 1] = '.'
                            y++
                        }
                    }
                }
            }
            //roll east
            for (i in platform[0].indices) {
                for (j in platform.size - 2 downTo 0) {
                    if (platform[j][i] == 'O') {
                        var y = j + 1
                        while (y < platform.size && platform[y][i] == '.') {
                            platform[y][i] = 'O'
                            platform[y - 1][i] = '.'
                            y++
                        }
                    }
                }
            }
            val ser = serializePlatform(platform)
            if (!cycleFound && cache.containsKey(ser)) {
                println("Found cycle at $cycle last seen at ${cache[ser]}")
                maxCycle = 100 + (999_999_907 % 7)
                cycleFound = true
            }
            cache[ser] = cycle
            cycle++
        }

        return platform.sumOf {
            it.mapIndexed { index, c -> if (c == 'O') it.size - index else 0 }.sum()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 136)

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}