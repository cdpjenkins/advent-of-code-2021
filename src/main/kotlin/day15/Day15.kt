package day15

import readInput
import java.lang.AssertionError
import java.util.*

fun main() {
    val input = readInput("Day15")

    println(day15Part1DijsktraMinPath(input))
    println(day15Part2DijkstraMinPathFiveTimesBigger(input))
}

fun day15Part2DijkstraMinPathFiveTimesBigger(input: List<String>): Int {
    val initialGrid = inputToGrid(input)

    val width = input[0].length
    val height = input.size

    val grid =
        (0..(height * 5) - 1).flatMap { y ->
            (0..(width * 5) - 1).map { x ->
                Point(x, y) to Square(Point(x, y), multipliedRiskValue(x, y, width, height, initialGrid), Int.MAX_VALUE,)
            }
        }.toMap()

    return dijkstraSearch(grid, Point(0, 0), Point(width * 5 - 1, height * 5 - 1))
}

fun multipliedRiskValue(x: Int, y: Int, width: Int, height: Int, initialGrid: Map<Point, Square>): Int {
    val xMod = x % width
    val yMod = y % height
    val xPos = x / width
    val yPos = y / height

    val increasedRisk = initialGrid[Point(xMod, yMod)]!!.risk + xPos + yPos - 1
    return increasedRisk.mod(9) + 1
}

fun day15Part1DijsktraMinPath(input: List<String>): Int {
    val width = input[0].length
    val height = input.size

    val grid = inputToGrid(input)

    return dijkstraSearch(grid, Point(0, 0), Point(width - 1, height - 1))
}

private fun inputToGrid(input: List<String>): Map<Point, Square> {
    val grid = input.withIndex().flatMap { (y, line) ->
        line.withIndex().map { (x, char) ->
            Point(x, y) to Square(Point(x, y), char.toString().toInt(), Integer.MAX_VALUE)
        }
    }.toMap()
    return grid
}

private fun dijkstraSearch(
    grid: Map<Point, Square>,
    origin: Point,
    destination: Point
): Int {
    val comparator = object : Comparator<Point> {
        override fun compare(o1: Point?, o2: Point?): Int =
            grid[o1]!!.distance - grid[o2]!!.distance
    }
    val unvisited = PriorityQueue<Point>(comparator)
    unvisited.addAll(grid.keys)
    grid[origin]?.distance = 0

    while (!unvisited.isEmpty() && !grid[destination]!!.visited) {
        val nextNode = unvisited.remove()
        visit(nextNode, grid, unvisited)
    }

    return grid[destination]!!.distance
}

private fun visit(
    currentNode: Point,
    grid: Map<Point, Square>,
    unvisited: PriorityQueue<Point>
) {
    val square = grid[currentNode] ?: throw AssertionError("Null square")
    val neighbours = grid.neighboursOf(currentNode.x, currentNode.y)
    val unvisitedNeighbours = neighbours.filter { !it.visited }

    for (neighbour in unvisitedNeighbours) {
        val newDistance = square.distance + neighbour.risk
        if (newDistance < neighbour.distance) {
            unvisited.remove(neighbour.location)
            neighbour.distance = newDistance
            unvisited.add(neighbour.location)
        }
    }
    square.visited = true
}

private fun Map<Point, Square>.neighboursOf(x: Int, y: Int): List<Square> =
    listOf(
        Point(x - 1, y),
        Point(x, y - 1),
        Point(x + 1, y),
        Point(x, y + 1)
    )
        .map { this[it] }
        .filterNotNull()

data class Point(val x: Int, val y: Int)
data class Square(
    val location: Point,
    val risk: Int,
    var distance: Int,
    var visited: Boolean = false
)

