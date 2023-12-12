fun main() {
    fun checkPattern(base: List<String>, bucket: Array<Int>, pattern: String, border: Int = Int.MAX_VALUE): Boolean {
        var idx = 0
        for (i in base.indices) {
            for (j in 0..<bucket[i]) if (pattern[idx + j] == '#') return false
            idx += bucket[i]
            if (i > 0) {
                if (pattern[idx] == '#') return false
                else idx++
            }
            for (j in base[i].indices) if (pattern[idx + j] == '.') return false
            idx += base[i].length
            if (i + 1 > border) return true
        }
        for (i in 0..<bucket.last()) if (pattern[idx + i] == '#') return false
        return true
    }

    var noSolutions: MutableSet<Pair<Int, Int>> = mutableSetOf()

    fun fillBucket(base: List<String>, pattern: String, bucket: Array<Int>, left: Int, idx: Int): List<Array<Int>> {
        if (noSolutions.contains(Pair(idx, left))) return listOf()
        return if (idx < bucket.size - 1)
            IntRange(0, left).map {
                val b = bucket.clone()
                b[idx] = it
                if (checkPattern(base, b, pattern, idx)) {
                    val solutions = fillBucket(base, pattern, b, left - it, idx + 1)
                    if (solutions.isEmpty())
                        noSolutions.add(Pair(idx + 1, left - it))
                    solutions
                } else
                    listOf()
            }.flatten()
        else {
            bucket[bucket.size - 1] = left
            if (checkPattern(base, bucket, pattern)) listOf(bucket) else listOf()
        }
    }

    var count = 0
    fun createOptions(base: List<String>, freeOperational: Int, pattern: String): Int {
        noSolutions = mutableSetOf()
        println("Pattern $count")
        count++
        return fillBucket(base, pattern, Array(base.size + 1) {0}, freeOperational, 0)
            .count()
    }

    fun part1(input: List<String>): Int {
        count = 0
        return input.sumOf {
            val (conditionRecord, groupSizes) = it.split(' ')
            val groups = groupSizes.split(',').map { it.toInt() }
            val fixed = groups.count() - 1 + groups.sum()
            val freeOperational = conditionRecord.length - fixed
            val base = groups.map { "#".repeat(it) }
            createOptions(base, freeOperational, conditionRecord)
        }
    }

    fun part2(input: List<String>): Int {
        count = 0
        return input.sumOf {
            val (conditionRecord, groupSizes) = it.split(' ')
            val cr = (conditionRecord + '?').repeat(5).dropLast(1)
            val groups = (groupSizes + ',').repeat(5).dropLast(1).split(',').map { it.toInt() }
            val fixed = groups.count() - 1 + groups.sum()
            val freeOperational = cr.length - fixed
            val base = groups.map { "#".repeat(it) }
            createOptions(base, freeOperational, cr)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 21)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}