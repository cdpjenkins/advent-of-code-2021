package day25

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

internal class Day25KtTest {
    @Test
    internal fun `east moving cucumbers move east if way is clear`() {
        val worlds = worldsSequence("...>>>>>...")
        assertThat(worlds.iteration(1), equalTo("...>>>>.>.."))
        assertThat(worlds.iteration(2), equalTo("...>>>.>.>."))
    }

    @Test
    internal fun `east moving cucumbers wrap when they reach the eastern edge of the world`() {
        assertThat(worldsSequence("..........>").iteration(1), equalTo(">.........."))
    }

    @Test
    internal fun `south facing cucumbers move south`() {
        val worldsSequence = worldsSequence(
            """
                .v
                ..
            """
        )
        assertThat(worldsSequence.iteration(1), equalTo(
            """
                ..
                .v
            """.trimIndent()
        ))
    }

    @Test
    internal fun `south facing cucumbers wrap when they reach the southern edge of the world`() {
        val worldsSequence = worldsSequence(
            """
                ..
                .v
            """

        )
        assertThat(worldsSequence.iteration(1), equalTo(
            """
                .v
                ..
            """.trimIndent()
        ))
    }
}

