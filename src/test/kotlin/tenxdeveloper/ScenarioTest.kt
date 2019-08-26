package tenxdeveloper

import org.junit.jupiter.api.Test


class ScenarioTest {

    @Test
    fun `it should rank the developer based on the given facts`(){
        val ranker = DeveloperRanker(
            facts = EnglishFactParser().parse(testInput())
        )
        assertk.assert(ranker.rank()!!).matchesRanking("Sarah", "John", "Jessie", "Evan", "Matt")
    }

    private fun testInput() = javaClass.getResourceAsStream("/input.txt").bufferedReader().readText()
}