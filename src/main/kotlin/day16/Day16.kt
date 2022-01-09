package day16

import readInput
import java.lang.AssertionError
import java.math.BigInteger

fun main() {
    val input = readInput("Day16").first()

    val versionSum = versionSum(input)
    println(versionSum)
}

fun versionSum(hex: String) =
    BinarySource.of(hex)
        .parsePacket()
        .versionSum()

private fun String.pad(): String {
    val bitsToPad = if (length % 4 != 0) {
        4 - (length % 4)
    } else {
        0
    }
    return "0".repeat(bitsToPad) + this
}


data class BinarySource(var binaryString: String) {
    fun parsePacket(): Packet {
        val version = take(3).toInt(2)
        val packetTypeId = take(3).toInt(2)

        if (packetTypeId == 4) {
            var accumulator = 0
            do {
                var done = false

                val chunk = take(5)
                if (chunk.first() == '0') {
                    done = true
                }

                accumulator *= 16
                accumulator += chunk.drop(1).toInt(2)
            } while (!done)

            return Literal(version, accumulator)
        } else {
            val lengthTypeID = take(1)

            when (lengthTypeID) {
                "0" -> {
                    val length = take(15).toInt(2)

                    val subPacketsSource = BinarySource(take(length))
                    val subPackets = mutableListOf<Packet>()
                    while (subPacketsSource.binaryString.isNotEmpty()) {
                        subPackets.add(subPacketsSource.parsePacket())
                    }
                    return Operator(version, subPackets)
                }
                "1" -> {
                    val numSubPackets = take(11).toInt(2)

                    val subPackets = mutableListOf<Packet>()
                    for (i in 1..numSubPackets) {
                        subPackets.add(parsePacket())
                    }
                    return Operator(version, subPackets)
                }
                else -> {
                    throw AssertionError(lengthTypeID)
                }
            }
        }
    }

    private fun take(bits: Int): String {
        val taken = binaryString.take(bits)
        binaryString = binaryString.drop(bits)
        return taken
    }

    companion object {
        private fun String.toBinary() = BigInteger(this, 16).toString(2).pad()
        fun of(hex: String) = BinarySource(hex.toBinary())
    }
}


sealed class Packet(val version: Int) {
    abstract fun versionSum(): Int
}

data class Literal(val literalVersion: Int, val value: Int): Packet(literalVersion) {
    override fun versionSum() = this.version
}

data class Operator(val operatorVersion: Int, val subPackets: List<Packet>): Packet(operatorVersion) {
    override fun versionSum() = this.version + subPackets.sumOf { it.versionSum() }
}
