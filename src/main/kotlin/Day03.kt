import java.lang.AssertionError

fun main() {
    val input = readInput("Day03")

    println(day3Part1CalculatePowerConsumption(input))
}

fun day3Part1CalculatePowerConsumption(input: List<String>): Int {
    val gamma = (0..input[0].length - 1)
        .map { input.findMostCommonBit(it) }
        .joinToString("")
    val epsilon = gamma.invert()

    return gamma.toInt(2) * epsilon.toInt(2)
}

private fun String.invert(): String =
    this.map(::flipBit)
        .joinToString("")

private fun String.flipBit(it: Char) =
    when (it) {
        '0' -> '1'
        '1' -> '0'
        else -> throw AssertionError(this)
    }

private fun List<String>.findMostCommonBit(position: Int): Char =
    this.map { it[position] }
        .groupingBy { it }
        .eachCount()
        .maxByOrNull { it.value }
        ?.key
        ?: throw AssertionError(map { it[position] })

