package day22

import readInput
import java.lang.Integer.max
import java.lang.Integer.min

fun main() {
    val input = readInput("Day22")

    println(countCubesInInitialisationSection(input))
    println(countCubesInAllRebootSteps(input))
}

fun List<Command>.compile(): List<Command> =
    this.fold(emptyList()) { acc, command -> acc + processCommand(command, acc) }

fun processCommand(newCommand: Command, commandsSoFar: List<Command>): List<Command> {
    val overlappingCommands = commandsSoFar.filter { it.cuboid overlapsWith newCommand.cuboid }
    val newCommands = overlappingCommands.map { newCommand.combineWith(it) }

    if (newCommand.action == Action.on) {
        return newCommands + newCommand
    } else {
        return newCommands
    }
}

fun countCubesInInitialisationSection(input: List<String>) =
    input.parse()
        // strictly speaking we need to check for cuboids that are only partially in the box... bvut there don't
        // appear to be any in the input
        .filter { it.cuboid.insideInitialisationSection() }
        .compile()
        .fold(0L) { acc, command -> acc + command.value() }

fun countCubesInAllRebootSteps(input: List<String>): Long =
    input.parse()
        .compile()
        .fold(0L) { acc, command -> acc + command.value() }

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

enum class Action {
    on, off;

    fun inverse() = when (this) {
        on -> off
        off -> on
    }

    fun direction() = when (this) {
        on -> 1
        off -> -1
    }
}

data class Cuboid(val xMin: Int,
                  val xMax: Int,
                  val yMin: Int,
                  val yMax: Int,
                  val zMin: Int,
                  val zMax: Int) {

    fun insideInitialisationSection(): Boolean =
        !((xMin < -50 && xMax < -50)
                || (xMin >= 50 && xMax >= 50)
                || (yMin <= -50 && yMax <= -50)
                || (yMin >= 50 && yMax >= 50)
                || (zMin <= -50 && zMax <= -50)
                || (zMin >= 50 && zMax >= 50))

    infix fun overlapsWith(that: Cuboid): Boolean {
        return this overlapsOnXAxis that &&
                this overlapsOnYAxis that &&
                this overlapsOnZAxis that
    }

    private infix fun overlapsOnXAxis(that: Cuboid) = this.xMin <= that.xMax && this.xMax >= that.xMin
    private infix fun overlapsOnYAxis(that: Cuboid) = this.yMin <= that.yMax && this.yMax >= that.yMin
    private infix fun overlapsOnZAxis(that: Cuboid) = this.zMin <= that.zMax && this.zMax >= that.zMin

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

    fun volume() = ((1 + xMax.toLong() - xMin.toLong())
            * (1 + yMax.toLong() - yMin.toLong())
            * (1 + zMax.toLong() - zMin.toLong()))
}

data class Command(
    val action: Action,
    val cuboid: Cuboid
) {
    fun combineWith(that: Command) = Command(that.action.inverse(), this.cuboid.intersectionWith(that.cuboid))
    fun value() = cuboid.volume() * action.direction()
}
