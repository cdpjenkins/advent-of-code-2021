package day15

import readInput
import java.lang.AssertionError

fun main() {
    val input = readInput("Day15")

    println(day15Part1DijsktraMinPath(input))
}

fun day15Part1DijsktraMinPath(input: List<String>): Int {
    val width = input[0].length
    val height = input.size

    val grid = input.withIndex().flatMap { (y, line) ->
        line.withIndex().map { (x, char) ->
            Point(x, y) to Square(x, y, char.toString().toInt(), Integer.MAX_VALUE)
        }
    }.toMap()

    val unvisited: MutableSet<Point> = grid.keys.toMutableSet()

    var currentNode: Point? = Point(0, 0)
    grid[currentNode]?.distance = 0

    while (currentNode != null) {
        println(currentNode)
        visit(currentNode, grid, unvisited)
        currentNode = unvisited.minByOrNull { grid[it]!!.distance }
    }

    return grid[Point(width - 1, height - 1)]!!.distance
}

private fun visit(
    currentNode: Point,
    grid: Map<Point, Square>,
    unvisited: MutableSet<Point>
) {
    val square = grid[currentNode] ?: throw AssertionError("Null square")
    val neighbours = grid.neighboursOf(currentNode.x, currentNode.y)
    val unvisitedNeighbours = neighbours.filter { !it.visited }

    for (neighbour in unvisitedNeighbours) {
        val newDistance = square.distance + neighbour.risk
        if (newDistance < neighbour.distance) {
            neighbour.distance = newDistance
        }
    }
    square.visited = true
    unvisited.remove(currentNode)
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
data class Square(val x: Int, val y: Int, val risk: Int, var distance: Int, var visited: Boolean = false)

