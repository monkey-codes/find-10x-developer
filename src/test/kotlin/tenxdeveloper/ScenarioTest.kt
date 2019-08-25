package tenxdeveloper

import org.junit.jupiter.api.Test


class ScenarioTest {

    @Test
    fun `it should rank the developer based on the given facts`(){
        val ranker = DeveloperRanker(
            facts = EnglishFactParser().parse(
                """|Jessie is not the best developer
                   |Evan is not the worst developer
                   |John is not the best developer or the worst developer
                   |Sarah is a better developer than Evan
                   |Matt is not directly below or above John as a developer
                   |John is not directly below or above Evan as a developer""".trimMargin()
            )
        )
        assertk.assert(ranker.rank()!!).matchesRanking("Sarah", "John", "Jessie", "Evan", "Matt")
    }
}