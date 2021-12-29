package day22

import readInput

fun main() {
    val input = readInput("Day22")

    println(countCubesTurnedOn(input))
}

fun countCubesTurnedOn(input: List<String>): Int {
    val cubes: MutableSet<Point3D> = mutableSetOf()
    val cuboids = input.parse()

    for (cuboid in cuboids) {
        cuboid.applyToReactor(cubes)
    }

    return cubes.size
}

private fun List<String>.parse(): List<Cuboid> {
    val regex = "(on|off) x=(-?[0-9]+)..(-?[0-9]+),y=(-?[0-9]+)..(-?[0-9]+),z=(-?[0-9]+)..(-?[0-9]+)".toRegex()

    return this.map {
        val (action, xMin, xMax, yMin, yMax, zMin, zMax) = regex.find(it)?.destructured ?: throw AssertionError(it)
        Cuboid(
            Action.valueOf(action),
            xMin.toInt(),
            xMax.toInt(),
            yMin.toInt(),
            yMax.toInt(),
            zMin.toInt(),
            zMax.toInt()
        )
    }
}

enum class Action { on, off }
data class Point3D(val x: Int, val y: Int, val z: Int)
data class Cuboid(
    val action: Action,
    val xMin: Int,
    val xMax: Int,
    val yMin: Int,
    val yMax: Int,
    val zMin: Int,
    val zMax: Int
) {
    fun applyToReactor(cubes: MutableSet<Point3D>) {
        if (inside50x50BoxAtAll()) {
            for (x in xMin..xMax) {
                for (y in yMin..yMax) {
                    for (z in zMin..zMax) {
                        when (action) {
                            Action.on -> {
                                cubes.add(Point3D(x, y, z))
                            }
                            Action.off -> {
                                cubes.remove(Point3D(x, y, z))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun inside50x50BoxAtAll(): Boolean =
        !((xMin < -50 && xMax < -50)
                || (xMin > 50 && xMax > 50)
                || (yMin < -50 && yMax < -50)
                || (yMin > 50 && yMax > 50)
                || (zMin < -50 && zMax < -50)
                || (zMin > 50 && zMax > 50))
}
