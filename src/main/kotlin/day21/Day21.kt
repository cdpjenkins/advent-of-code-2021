package day21

import java.io.File
import java.lang.AssertionError

fun main() {
    val input = File("src/main/resources", "Day21.txt").readText()

    println(day21Part1DeterministicDice(input))
}


fun day21Part1DeterministicDice(input: CharSequence): Int {
    val regex = Regex(
        "Player 1 starting position: ([0-9]{1,2})\nPlayer 2 starting position: ([0-9]{1,2})"
    )

    val (p1Start, p2Start) = regex.find(input)
        ?.destructured
        ?.toList()
        ?.map { it.toInt() }
        ?: throw AssertionError("bang")

    println("$p1Start, $p2Start")

    val game = Game(
        player1Score = 0,
        player2Score = 0,
        player1Position = p1Start,
        player2Position = p2Start
    )

    while (game.winner() == null) {
        game.playTurn()
        println("turn ${game.turnsPlayed} $game")
    }

    val losingScore = game.losingScore()

    val result = losingScore * game.diceLastValue
    return result
}


data class Game(
    var player1Score: Int,
    var player2Score: Int,
    var player1Position: Int,
    var player2Position: Int,
    var diceLastValue: Int = 0,
    var nextPlayerTurn: Int = 1
) {
    var turnsPlayed: Int = 0

    fun playTurn() {
        turnsPlayed++

        val diceValue1 = ++diceLastValue
        val diceValue2 = ++diceLastValue
        val diceValue3 = ++diceLastValue

        val diceTotal = diceValue1 + diceValue2 + diceValue3

        when (nextPlayerTurn) {
            1 -> {
                player1Position += diceTotal
                player1Position = ((player1Position - 1) % 10) + 1
                player1Score += player1Position
            }
            2 -> {
                player2Position += diceTotal
                player2Position = ((player2Position - 1) % 10) + 1
                player2Score += player2Position
            }
        }

        nextPlayerTurn = when (nextPlayerTurn) {
            1 -> 2
            2 -> 1
            else -> throw AssertionError("waaaa")
        }
    }

    fun winner(): Int? {
        if (player1Score >= 1000) return 1
        if (player2Score >= 1000) return 2
        return null
    }

    fun losingScore(): Int =
        when {
            winner() == 1 -> player2Score
            winner() == 2 -> player1Score
            else -> throw AssertionError("urgh")
        }
}

