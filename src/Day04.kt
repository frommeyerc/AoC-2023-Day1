fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            val split = it.split(':','|')
            val winning = split[1].trim().split(' ')
            val having = split[2].trim().split(' ')
            having.filter { it.isNotBlank() }.count {
                winning.contains(it)
            }.let {
                if (it > 0) 1 shl (it - 1) else 0
            }
        }
    }

    fun part2 (input: List<String>): Int {
        val cardStackMap = mutableMapOf<Int, Int>()
        input.forEach {
            val split = it.split(':','|')
            val cardNumber = "[0-9]+".toRegex().find(split[0])!!.value.toInt()
            cardStackMap.compute(cardNumber) { _, v -> if (v == null) 1 else v + 1 }
            val winning = split[1].trim().split(' ')
            val having = split[2].trim().split(' ')
            having.filter { it.isNotBlank() }.count {
                winning.contains(it)
            }.let {
                val numCards = cardStackMap[cardNumber]!!
                for (i in 1..it)
                    cardStackMap.compute(cardNumber + i) { _, v -> if (v == null) numCards else v + numCards}
            }
        }
        return cardStackMap.values.take(input.size).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}