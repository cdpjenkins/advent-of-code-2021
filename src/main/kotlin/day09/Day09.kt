package day09

import readInput

fun main() {
    val input = readInput("Day09")

    println(sumOfRiskLevelsOfLowPoints(input))
}

fun sumOfRiskLevelsOfLowPoints(input: List<String>): Int {
    val heightMap = input.toHeightMap()
    val lowPoints = heightMap.keys.filter { it.isLowPoint(heightMap) }
    return lowPoints.map { heightMap[it]!! + 1 }.sum()
}

private fun List<String>.toHeightMap(): HashMap<Point, Int> {
    val locations = HashMap<Point, Int>()
    for ((y: Int, line: String) in withIndex()) {
        for ((x, c) in line.withIndex()) {
            locations.put(Point(x, y), c.toString().toInt())
        }
    }
    return locations
}

private fun Point.isLowPoint(locations: Map<Point, Int>) =
    this.neighbourValues(locations)
        .filter { it <= locations[this]!! }
        .isEmpty()

fun Point.neighbourValues(locations: Map<Point, Int>): List<Int> =
    neighbourCoords()
        .map { locations[it] }
        .filterNotNull()

fun Point.neighbourCoords(): List<Point> =
    listOf(
        Point(x - 1, y),
        Point(x + 1, y),
        Point(x, y - 1),
        Point(x, y + 1)
    )

data class Point(val x: Int, val y: Int)
