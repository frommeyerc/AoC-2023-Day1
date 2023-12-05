fun main() {
    fun part1(input: List<String>): Long {
        val iterator = input.iterator()
        val seeds = iterator.next().drop(7).split(' ').map { it.toLong() }
        val seedToSoil = PuzzleMap(iterator)
        val soilToFertilizer = PuzzleMap(iterator)
        val fertilizerToWater = PuzzleMap(iterator)
        val waterToLight = PuzzleMap(iterator)
        val lightToTemperature = PuzzleMap(iterator)
        val temperatureToHumidity = PuzzleMap(iterator)
        val humidityToLocation = PuzzleMap(iterator)
        return seeds.map { seedToSoil.lookUp(it) }.map { soilToFertilizer.lookUp(it) }
            .map { fertilizerToWater.lookUp(it) }
            .map { waterToLight.lookUp(it) }.map { lightToTemperature.lookUp(it) }
            .map { temperatureToHumidity.lookUp(it) }.minOf { humidityToLocation.lookUp(it) }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

class PuzzleMap(iterator: Iterator<String>) {
    private val entries: MutableList<Triple<Long, Long, Long>> = mutableListOf()
    private val name: String

    init {
        var line = iterator.next()
        while (line.isEmpty()) line = iterator.next()
        name = line.removeSuffix(" map:")
        line = iterator.next()
        while (line.isNotBlank()) {
            val split = line.split(' ')
            addEntry(split[0].toLong(), split[1].toLong(), split[2].toLong())
            line = if (iterator.hasNext()) iterator.next() else ""
        }
    }
    fun addEntry(destination: Long, source: Long, length: Long) {
        entries.add(Triple(destination, source, length))
    }

    fun lookUp(source: Long): Long {
        val entry = entries.firstOrNull { it.second <= source && it.second + it.third > source }
        return if (entry == null) source else entry.first + (source - entry.second);
    }
}