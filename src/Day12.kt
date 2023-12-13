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
        count++
        val start = System.currentTimeMillis()
        try {
            return fillBucket(base, pattern, Array(base.size + 1) { 0 }, freeOperational, 0)
                .count()
        } finally {
            val time = System.currentTimeMillis() - start;
            println("Pattern $count:  $time ms")
        }
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

    fun arrangements(spread: String, compact: List<Int>, cache: MutableMap<String, Long> = hashMapOf()): Long {
        var count = 0L
        for (i in spread.indices) {
            if (i > 0 && spread[i - 1] == '#') return count
            val char = spread[i]
            if (char == '.') continue
            if (compact.isEmpty() && char == '#') return 0
            if (compact.isEmpty()) continue
            val matchSize = compact.first()
            if (i + matchSize > spread.length) continue // could be break?
            val spreadEquivalent = spread.substring(i, i + matchSize)
            val isMatch = spreadEquivalent.all { it != '.' }
            val hasDelimiter = i + matchSize == spread.length || spread[i + matchSize] != '#'
            if (isMatch) count += when {
                i + matchSize == spread.length -> arrangements("", compact.drop(1), cache)
                hasDelimiter -> {
                    val next = spread.substring(i + matchSize + 1)
                    val nextCompact = compact.drop(1)
                    val key = "$next-${nextCompact.hashCode()}"
                    cache.getOrPut(key) { arrangements(next, nextCompact, cache) }
                }
                else -> 0
            }
        }
        return if (compact.isEmpty()) 1 else count
    }
    fun part2_2(input: List<String>): Long {
        return input.sumOf {
            val (conditionRecord, groupSizes) = it.split(' ')
            val cr = ("$conditionRecord?").repeat(5).dropLast(1)
            val groups = ("$groupSizes,").repeat(5).dropLast(1).split(',').map { it.toInt() }
            arrangements(cr, groups)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 21)

    val input = readInput("Day12")
    part1(input).println()
    part2_2(input).println()
}