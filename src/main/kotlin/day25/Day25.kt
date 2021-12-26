package day25

fun main() {

}

fun Sequence<World>.iteration(i: Int) = elementAt(i).toPrintableString()
fun worldsSequence(input: String) = generateSequence(worldFrom(input.trimIndent())) { it.move() }
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
        val moveEast = moveEast()
        val moveSouth = moveEast.moveSouth()
        return moveSouth
    }

    private fun moveEast(): World {
        val newPoints = (0..height - 1).flatMap { y ->
            (0..width - 1).map { x ->
                val value = pointAt(x, y)
                val newValue = when (value) {
                    '.' -> if (pointAt(x - 1, y) == '>') '>' else '.'
                    '>' -> if (pointAt(x + 1, y) == '.') '.' else '>'
                    'v' -> 'v'
                    else -> '.'
                }
                Point(x, y) to newValue
            }
        }.toMap()

        return World(newPoints, width, height)
    }

    private fun moveSouth(): World {
        val newPoints = (0..height - 1).flatMap { y ->
            (0..width - 1).map { x ->
                val value = pointAt(x, y)
                val newValue = when (value) {
                    '.' -> if (pointAt(x, y - 1) == 'v') 'v' else '.'
                    'v' -> if (pointAt(x, y + 1) == '.') '.' else 'v'
                    '>' -> '>'
                    else -> '.'
                }
                Point(x, y) to newValue
            }
        }.toMap()

        return World(newPoints, width, height)
    }

    private fun pointAt(x: Int, y: Int) = points[Point(x.mod(width), y.mod(height))]
}

data class Point(val x: Int, val y: Int)
