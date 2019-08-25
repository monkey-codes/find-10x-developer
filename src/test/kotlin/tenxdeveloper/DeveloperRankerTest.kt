package tenxdeveloper

import assertk.Assert
import assertk.assert
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import org.junit.Test


class DeveloperRankerTest {

    @Test
    fun `it should rank based on the best`() {
        val ranker = DeveloperRanker<String>(
            facts = {
                developer("Evan") { isNot(theBest()) }
                developer("Jessie") { `is`(theBest()) }
            }
        )
        assert(ranker.rank()!!).matchesRanking("Jessie", "Evan")
    }

    @Test
    fun `it should rank based on the worst`() {
        val ranker = DeveloperRanker<String>(
            facts = {
                developer("Evan") { isNot(theWorst()) }
                developer("Jessie") { `is`(theWorst()) }
            }
        )
        assert(ranker.rank()!!).matchesRanking("Evan", "Jessie")
    }

    @Test
    fun `it should rank based on better than`() {
        val ranker = DeveloperRanker<String>(
            facts = {
                developer("Evan") { isBetterThan("Jessie") }
            }
        )
        assert(ranker.rank()!!).matchesRanking("Evan", "Jessie")
    }

    @Test
    fun `it should rank based on not directly below`() {
        val ranker = DeveloperRanker<String>(
            facts = {
                developer("Evan") { `is`(directlyBelow("Jessie")) }
            }
        )
        assert(ranker.rank()!!).matchesRanking("Jessie", "Evan")
    }

    @Test
    fun `it should rank based on not directly above`() {
        val ranker = DeveloperRanker<String>(
            facts = {
                developer("Evan") { `is`(directlyAbove("Jessie")) }
            }
        )
        assert(ranker.rank()!!).matchesRanking("Evan", "Jessie")
    }

    @Test
    fun `it should rank developers based on what is known`() {
        val ranker = DeveloperRanker<String>(facts = {
            developer("Jessie") { isNot(theBest()) }
            developer("Evan") { isNot(theWorst()) }
            developer("John") { isNot(theBest(), or, theWorst()) }
            developer("Sarah") { isBetterThan("Evan") }
            developer("Matt") { isNot(directlyBelow("John"), or, directlyAbove("John")) }
            developer("John") { isNot(directlyBelow("Evan"), or, directlyAbove("Evan")) }
        })
        assert(ranker.rank()!!).matchesRanking("Sarah", "John", "Jessie", "Evan", "Matt")
    }
}

fun <E> Assert<Ranking<E>>.matchesRanking(
    vararg order: E
) {
    prop(Ranking<String>::order).containsExactly(*order)
    prop(Ranking<E>::tenXDeveloper).isEqualTo(order[0])
}