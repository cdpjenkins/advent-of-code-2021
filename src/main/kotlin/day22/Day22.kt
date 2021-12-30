package day22

import readInput
import java.lang.Integer.max
import java.lang.Integer.min

fun main() {
    val input = readInput("Day22")

    println(countCubesTurnedOn(input))

    val cuboids = input.parse()
    for ((i, cuboid) in cuboids.withIndex()) {
        println(cuboid)
        println(cuboids.filter { cuboid.cuboid overlapsWith it.cuboid }.filter {it.action == Action.on }.size)
        println(cuboids.filter { cuboid.cuboid overlapsWith it.cuboid }.filter {it.action == Action.off }.size)
    }
}

fun countCubesTurnedOn(input: List<String>): Int {
    val cubes: MutableSet<Point3D> = mutableSetOf()
    val cuboids = input.parse()

    for (cuboid in cuboids) {
        cuboid.applyToReactor(cubes)
    }

    return cubes.size
}

private fun List<String>.parse(): List<Command> {
    return this.map { command(it) }
}

val regex = "(on|off|intersection) x=(-?[0-9]+)..(-?[0-9]+),y=(-?[0-9]+)..(-?[0-9]+),z=(-?[0-9]+)..(-?[0-9]+)".toRegex()

fun command(it: String): Command {
    val (action, xMin, xMax, yMin, yMax, zMin, zMax) = regex.find(it)?.destructured ?: throw AssertionError(it)
    return Command(
        Action.valueOf(action),
        Cuboid(
            xMin.toInt(),
            xMax.toInt(),
            yMin.toInt(),
            yMax.toInt(),
            zMin.toInt(),
            zMax.toInt()
        )
    )
}

enum class Action { on, off, intersection }
data class Point3D(val x: Int, val y: Int, val z: Int)
data class Cuboid(val xMin: Int,
                  val xMax: Int,
                  val yMin: Int,
                  val yMax: Int,
                  val zMin: Int,
                  val zMax: Int) {

    fun inside50x50BoxAtAll(): Boolean =
        !((xMin < -50 && xMax < -50)
                || (xMin >= 50 && xMax >= 50)
                || (yMin <= -50 && yMax <= -50)
                || (yMin >= 50 && yMax >= 50)
                || (zMin <= -50 && zMax <= -50)
                || (zMin >= 50 && zMax >= 50))

    infix fun overlapsWith(that: Cuboid): Boolean {
        return this overlapsOnXProjection that &&
                this overlapsOnYProjection that &&
                this overlapsOnZProjection that
    }

    private infix fun overlapsOnXProjection(that: Cuboid) = this.xMin <= that.xMax && this.xMax >= that.xMin
    private infix fun overlapsOnYProjection(that: Cuboid) = this.yMin <= that.yMax && this.yMax >= that.yMin
    private infix fun overlapsOnZProjection(that: Cuboid) = this.zMin <= that.zMax && this.zMax >= that.zMin

    fun intersectionWith(that: Cuboid): Cuboid {
        return Cuboid(
            max(this.xMin, that.xMin),
            min(this.xMax, that.xMax),
            max(this.yMin, that.yMin),
            min(this.yMax, that.yMax),
            max(this.zMin, that.zMin),
            min(this.zMax, that.zMax)
        )
    }

}

data class Command(
    val action: Action,
    val cuboid: Cuboid
) {
    fun applyToReactor(cubes: MutableSet<Point3D>) {
        if (cuboid.inside50x50BoxAtAll()) {
            for (x in cuboid.xMin..cuboid.xMax) {
                for (y in cuboid.yMin..cuboid.yMax) {
                    for (z in cuboid.zMin..cuboid.zMax) {
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

}
