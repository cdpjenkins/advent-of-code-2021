package day21

import java.io.File
import kotlin.AssertionError

fun main() {
    val input = File("src/main/resources", "Day21.txt").readText()

    println(day21Part1DeterministicDice(input))
}


fun day21Part1DeterministicDice(input: CharSequence): Int {
    val regex = "Player 1 starting position: ([0-9]{1,2})\nPlayer 2 starting position: ([0-9]{1,2})".toRegex()
    val (p1Start, p2Start) = regex.find(input)
        ?.destructured
        ?.toList()
        ?.map { it.toInt() }
        ?: throw AssertionError("bang")

    val game = DeterministicGame(
        gameState = GameState(
            player1State = PlayerState(
                score = 0,
                position = p1Start
            ),
            player2State = PlayerState(
                score = 0,
                position = p2Start
            )
        )
    )

    while (game.gameState.winner() == null) {
        game.playTurn()
        println("turn ${game.turnsPlayed} $game")
    }

    val losingScore = game.gameState.losingScore()

    return losingScore * game.diceLastValue
}

data class PlayerState(
    val score: Int,
    val position: Int
) {
    fun playOnePlayerTurn(diceTotal: Int) = PlayerState(
        position = modPositionRange(position + diceTotal),
        score = score + modPositionRange(position + diceTotal)
    )

    private fun modPositionRange(position: Int) = ((position - 1) % 10) + 1
}

data class GameState(
    val player1State: PlayerState,
    val player2State: PlayerState,
    val nextPlayerTurn: Int = 1
) {
    fun playOneTurn(diceTotal: Int): GameState {
        val nextGameState = when (nextPlayerTurn) {
            1 -> {
                copy(
                    player1State = player1State.playOnePlayerTurn(diceTotal)
                )
            }
            2 -> {

                copy(
                    player2State = player2State.playOnePlayerTurn(diceTotal)
                )
            }
            else -> throw AssertionError("Bad nextPlayerTurn: $nextPlayerTurn")
        }

        return nextGameState.copy(nextPlayerTurn = nextPlayer(nextGameState.nextPlayerTurn))
    }

    private fun nextPlayer(currentPlayer: Int) = 2 - currentPlayer + 1

    fun losingScore(): Int =
        when {
            winner() == 1 -> player2State.score
            winner() == 2 -> player1State.score
            else -> throw AssertionError("urgh")
        }

    fun winner(): Int? {
        if (player1State.score >= 1000) return 1
        if (player2State.score >= 1000) return 2
        return null
    }
}

data class DeterministicGame(
    var diceLastValue: Int = 0,
    var gameState: GameState
) {
    var turnsPlayed: Int = 0

    fun playTurn() {
        turnsPlayed++

        val diceValue1 = ++diceLastValue
        val diceValue2 = ++diceLastValue
        val diceValue3 = ++diceLastValue

        val diceTotal = diceValue1 + diceValue2 + diceValue3

        gameState = gameState.playOneTurn(diceTotal)
    }
}

