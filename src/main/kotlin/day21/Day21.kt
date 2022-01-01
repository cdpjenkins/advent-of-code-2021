package day21

import java.io.File
import kotlin.AssertionError

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

    val game = DeterministicGame(
        gameState = GameState(
            player1Score = 0,
            player2Score = 0,
            player1Position = p1Start,
            player2Position = p2Start
        )
    )

    while (game.winner() == null) {
        game.playTurn()
        println("turn ${game.turnsPlayed} $game")
    }

    val losingScore = game.losingScore()

    val result = losingScore * game.diceLastValue
    return result
}

data class GameState(
    var player1Score: Int,
    var player2Score: Int,
    var player1Position: Int,
    var player2Position: Int,
    var nextPlayerTurn: Int = 1
)

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

        gameState = playOneTurn(diceTotal)
    }

    private fun playOneTurn(diceTotal: Int): GameState {
        val nextGameState = when (gameState.nextPlayerTurn) {
            1 -> {
                val newPlayer1Position = modPositionRange(gameState.player1Position + diceTotal)
                val newPlayer1Score = gameState.player1Score + newPlayer1Position

                gameState.copy(player1Position = newPlayer1Position, player1Score = newPlayer1Score)
            }
            2 -> {
                val newPlayer2Position = modPositionRange(gameState.player2Position + diceTotal)
                val newPlayer2Score = gameState.player2Score + newPlayer2Position

                gameState.copy(player2Position = newPlayer2Position, player2Score = newPlayer2Score)
            }
            else -> throw AssertionError("Bad nextPlayerTurn: ${gameState.nextPlayerTurn}")
        }

        return nextGameState.copy(nextPlayerTurn = nextPlayer(nextGameState.nextPlayerTurn))
    }

    private fun nextPlayer(currentPlayer: Int) = 2 - currentPlayer + 1

    private fun modPositionRange(position: Int) = ((position - 1) % 10) + 1

    fun winner(): Int? {
        if (gameState.player1Score >= 1000) return 1
        if (gameState.player2Score >= 1000) return 2
        return null
    }

    fun losingScore(): Int =
        when {
            winner() == 1 -> gameState.player2Score
            winner() == 2 -> gameState.player1Score
            else -> throw AssertionError("urgh")
        }
}

