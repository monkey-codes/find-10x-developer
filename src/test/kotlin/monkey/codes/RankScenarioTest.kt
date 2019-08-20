package monkey.codes

import org.junit.Test


class RankScenarioTest {

    @Test
    fun `it should rank developers based on what is known`() {
        val firstMatchWithWhatWeKnow = with(PredicateBuilder<String>()) {
            whatWeKnow(
                developer("Matt") { isNot(theBest()) },
                developer("Evan") { isNot(theWorst()) },
                developer("John") { isNot(theBest(), or, theWorst()) },
                developer("Sarah") { isBetterThan("Evan") },
                developer("Matt") { isNot(directlyBelow("John"), or, directlyAbove("John"))},
                developer("John") { isNot(directlyBelow("Evan"), or, directlyAbove("Evan"))}
            )
        }
        val result = listOf("Jessie", "Evan", "John", "Sarah", "Matt").permutations(firstMatchWithWhatWeKnow)

        println(result)
    }
}