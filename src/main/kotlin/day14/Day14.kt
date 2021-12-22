package day14

import readInput
import kotlin.AssertionError

fun main() {
    val input = readInput("Day14")

    println(day14Part1(input))
}

fun day14Part1(input: List<String>): Int {
    val (polymer, rules) = parseInput(input)

    val resultingPolymer = generateSequence(polymer) { processStep(it, rules) }
        .drop(10)
        .first()
    val frequencies = resultingPolymer.groupingBy { it }.eachCount()

    val max = frequencies.values.maxOrNull() ?: throw AssertionError("curses!!!1")
    val min = frequencies.values.minOrNull() ?: throw AssertionError("arghghgh!!!1")

    return max - min
}

fun processStep(
    polymer: String,
    rulesMap: Map<String, String>
): String =
    polymer
        .zip(polymer.drop(1))
        .map {
            it.first + (rulesMap["${it.first}${it.second}"] ?: throw AssertionError("Waaaaa"))
        }
        .joinToString("")
        .plus(polymer.last())

fun parseInput(input: List<String>): Pair<String, Map<String, String>> {
    val polymer = input.first()

    val insertionRules = input.drop(2)

    val rulesMap = insertionRules
        .map { it.parseRuleLine() }
        .toMap()
    return Pair(polymer, rulesMap)
}

fun String.parseRuleLine(): Pair<String, String> {
    val regex = "([A-Z]{2}) -> ([A-Z])".toRegex()

    val (pair, toInsert) = regex.find(this)
        ?.destructured
        ?: throw AssertionError("Oh noes!")

    return pair to toInsert
}

