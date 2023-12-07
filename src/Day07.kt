fun main() {
    fun part1(input: List<String>): Int {
        return input.map { it.split(' ') }.map { Hand(it[0], it[1]) }.sorted()
            .mapIndexed { index, hand -> (index + 1) * hand.bid }.sum()
    }

    fun part2(input: List<String>): Long {
        return input.map { it.split(' ') }.map { Hand(it[0], it[1]) }.sorted()
            .mapIndexed { index, hand -> (index + 1) * hand.bid.toLong() }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    //check(part1(testInput) == 6440)
    check(part2(testInput) == 5905L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

enum class HandType {
    HighCard,
    OnePair,
    TwoPairs,
    ThreeOfAKind,
    FullHouse,
    FourOfAKind,
    FiveOfAKind;

    companion object {
        fun typeOfHand(hand: String): HandType {
            val myHand = hand.map { it }.sorted().plus('X')
            var c = myHand[0]
            var longRun = 0
            var sndRun = 0
            var runLength = 0
            var jokers = 0
            myHand.forEach {
                if (it == 'J') jokers++
                else
                    if (it == c)
                        runLength++
                    else {
                        c = it
                        if (runLength > longRun) {
                            val tmp = longRun
                            longRun = runLength
                            runLength = tmp
                        }
                        if (runLength > sndRun)
                            sndRun = runLength
                        runLength = 1
                    }
            }
            longRun += jokers
            return when (longRun) {
                5 -> FiveOfAKind
                4 -> FourOfAKind
                3 -> if (sndRun == 2) FullHouse else ThreeOfAKind
                2 -> if (sndRun == 2) TwoPairs else OnePair
                else -> HighCard
            }
        }
    }
}

class Hand(hand: String, bidStr: String) : Comparable<Hand> {
    private val cards: String
    private val type: HandType
    val bid: Int
    init {
        cards = hand
        type = HandType.typeOfHand(cards)
        bid = bidStr.toInt()
    }

    override fun toString(): String {
        return "[cards: $cards, type: $type, bid: $bid]"
    }

    fun cardValue(c: Char): Int {
        return when (c) {
            in '2'..'9' -> "$c".toInt()
            'T' -> 10
            'J' -> 1
            'Q' -> 12
            'K' -> 13
            'A' -> 14
            else -> 0
        }
    }
    fun compareSingleCards(other: String): Int {
        return cards.zip(other).first { it.first != it.second }.let { cardValue(it.first) - cardValue(it.second) }
    }

    override fun compareTo(other: Hand): Int {
        return if (type == other.type) return compareSingleCards(other.cards) else type.compareTo(other.type)
    }
}