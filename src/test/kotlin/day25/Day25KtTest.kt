package day25

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

internal class Day25KtTest {
    @Test
    internal fun `east moving cucumbers move east if way is clear`() {
        val worlds = worldsSequence("...>>>>>...")
        assertThat(iteration(worlds, 1), equalTo("...>>>>.>..".trimIndent()))
        assertThat(iteration(worlds, 2), equalTo("...>>>.>.>.".trimIndent()))
    }
}

private fun iteration(worlds: Sequence<World>, i: Int) = worlds.drop(i).first().toPrintableString()
private fun worldsSequence(input: String) = generateSequence(worldFrom(input)) { it.move() }
private fun worldFrom(input: String) = input.trimIndent().lines().parse()

private fun List<String>.parse(): World {
    val points = withIndex().flatMap { (y, line) ->
        line.withIndex().map { (x, point) -> Point(x, y) to point }
    }.toMap()
    val width = this[0].length
    val height = size

    return World(points, width, height)
}

data class World(val points: Map<Point, Char>, val width: Int, val height: Int) {
    fun toPrintableString() =
        (0..height - 1).map { y ->
            (0..width - 1).map { x ->
                points[Point(x, y)]
            }.joinToString("")
        }.joinToString("\n")

    fun move(): World {
        val newPoints = (0..height - 1).flatMap { y ->
            (0..width - 1).map { x ->
                val value = points[Point(x, y)]
                val newValue = when (value) {
                    '.' -> if (points[Point(x - 1, y)] == '>') '>' else '.'
                    '>' -> if (points[Point(x + 1, y)] == '.') '.' else '>'
                    else -> '.'
                }
                Point(x, y) to newValue
            }
        }.toMap()

        return World(newPoints, width, height)
    }
}

data class Point(val x: Int, val y: Int)