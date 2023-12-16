fun main() {

    fun hashi(s: String): Int {
        return s.fold(0) { acc, c ->
            ((acc + c.code) * 17) % 256
        }
    }

    fun part1(input: List<String>): Int {
        return input[0].split(',').sumOf { hashi(it) }
    }

    fun part2(input: List<String>): Int {
        val boxes = mutableMapOf<Int, MutableList<Pair<String, Int>>>()
        var regex = """([a-z]+)(?:(-)|=([0-9]))""".toRegex()
        input[0].split(',').map { regex.find(it) }.forEach() {
            val hash = hashi(it!!.groupValues[1])
            val box = boxes.computeIfAbsent(hash) { mutableListOf() }
            if (it.groupValues[2].isNotEmpty()) {
                //Minus
                box.removeIf { e-> e.first == it.groupValues[1] }
            } else {
                val idx = box.indexOfFirst { e -> e.first == it.groupValues[1] }
                if ( idx >= 0) {
                    box.removeAt(idx)
                    box.add(idx, Pair(it.groupValues[1], it.groupValues[3].toInt()))
                }
                else boxes.get(hash)!!.add(Pair(it.groupValues[1], it.groupValues[3].toInt()))
            }
        }
        return boxes.map {(key: Int, box: List<Pair<String, Int>>) ->
            box.mapIndexed { index, pair ->
                (key + 1) * (index + 1) * pair.second
            }
        }.flatten().sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 1320)

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}