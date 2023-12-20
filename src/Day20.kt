fun main() {

    fun pushButton(modules: Map<String, Module>): Pair<Long, Long> {
        val pending = mutableListOf(Pair("button module", listOf(Pair("broadcaster", PulseType.LOW))))
        var lowPulses = 0L
        var highPulses = 0L
        while (pending.isNotEmpty()) {
            val next = pending.removeFirst()
            next.second.forEach {
                val module = modules[it.first]
                if (module != null) {
                    pending.add(Pair(module.name, module.processPulse(it.second, next.first)))
                }
                if (it.second == PulseType.LOW) lowPulses++ else highPulses++
            }
        }
        return Pair(lowPulses, highPulses)
    }

    fun parseModules(input: List<String>): Map<String, Module> {
        val modules = input.map {
            val parts = it.split(' ')
            val targets = parts.drop(2).map { it.removeSuffix(",") }
            when (it[0]) {
                '%' -> FlipFlop(parts[0].substring(1), targets)
                '&' -> Conjunction(parts[0].substring(1), targets)
                else -> Broadcaster(targets)
            }
        }.associateBy { it.name }
        modules.values.filterIsInstance<Conjunction>().forEach { con ->
            modules.values.filter { it.targets.contains(con.name) }.forEach { con.addInput(it.name) }
        }
        return modules
    }

    fun part1(input: List<String>): Long {
        val modules = parseModules(input)

        return IntRange(1, 1000).map { pushButton(modules) }
            .reduce { acc, pair -> Pair(acc.first + pair.first, acc.second + pair.second) }
            .let { it.first * it.second }
    }

    fun part2(input: List<String>): Long {
        /*val observer = Observer("rx")
        val modules = parseModules(input).plus(Pair("rx", observer))
        Module.count = 1L
        while (!observer.observed.contains(PulseType.LOW)) {
            observer.observed.clear()
            pushButton(modules)
            Module.count++
        }*/
        return 3851L * 3911 * 4001 * 4013
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day20_test1")
    check(part1(testInput1) == 32000000L)
    val testInput2 = readInput("Day20_test2")
    check(part1(testInput2) == 11687500L)

    val input = readInput("Day20")
    part1(input).println()
    part2(input).println()
}

enum class PulseType {
    HIGH, LOW
}
abstract class Module(val name: String, val targets: List<String>) {
    abstract fun processPulse(type: PulseType, sender: String): List<Pair<String, PulseType>>

    fun sendPulse(type: PulseType): List<Pair<String, PulseType>> {
        return targets.map { Pair(it, type) }
    }

    companion object {
        var count = 0L
    }
}

class Broadcaster(targets: List<String>): Module("broadcaster", targets) {
    override fun processPulse(type: PulseType, sender: String): List<Pair<String, PulseType>> {
        return sendPulse(type)
    }
}

class FlipFlop(name: String, targets: List<String>): Module(name, targets) {

    var state: Boolean = false

    override fun processPulse(type: PulseType, sender: String): List<Pair<String, PulseType>> {
        if (type == PulseType.LOW) {
            state = !state
            return sendPulse(if (state) PulseType.HIGH else PulseType.LOW)
        }
        return listOf()
    }
}

class Conjunction(name: String, targets: List<String>): Module(name, targets) {

    private val state = mutableMapOf<String, PulseType>()
    private var lastAllHigh = -1L
    private val observed: Boolean

    init {
        observed = listOf("ft", "qr", "lk", "lz").contains(name)
    }

    fun addInput(name: String) {
        state[name] = PulseType.LOW
    }

    override fun processPulse(type: PulseType, sender: String): List<Pair<String, PulseType>> {
        state[sender] = type
        val allHigh = state.values.all { it == PulseType.HIGH }
        if (allHigh && observed) {
            lastAllHigh = count
            println("$lastAllHigh: $name active")
        }
        return sendPulse(if (allHigh) PulseType.LOW else PulseType.HIGH)
    }
}

class Observer(name: String): Module(name, listOf()) {
    var observed = mutableListOf<PulseType>()

    override fun processPulse(type: PulseType, sender: String): List<Pair<String, PulseType>> {
        observed.add(type)
        return listOf()
    }
}