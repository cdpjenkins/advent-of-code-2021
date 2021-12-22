package day21

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

internal class Day21KtTest {
    @Test
    internal fun `day 21 part 1 deterministic dice`() {
        val result = day21Part1DeterministicDice(input)
        assertThat(result, equalTo(739785))

    }

}

var input = """
            Player 1 starting position: 4
            Player 2 starting position: 8
        """.trimIndent()
