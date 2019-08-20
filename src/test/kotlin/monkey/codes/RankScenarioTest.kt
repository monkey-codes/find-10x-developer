package monkey.codes

import assertk.Assert
import assertk.assertions.support.expected
import org.junit.Test


class RankScenarioTest {

    @Test
    fun `it should rank developers based on what is known`() {
        val rank = DeveloperRank<String>(facts = {
            developer("Jessie") { isNot(theBest()) }
            developer("Evan") { isNot(theWorst()) }
            developer("John") { isNot(theBest(), or, theWorst()) }
            developer("Sarah") { isBetterThan("Evan") }
            developer("Matt") { isNot(directlyBelow("John"), or, directlyAbove("John")) }
            developer("John") { isNot(directlyBelow("Evan"), or, directlyAbove("Evan")) }
        })
        println(rank.order)
        println(rank.tenXDeveloper)
    }
}

fun <E> Assert<List<E>>.matchesWhatWeKnow(predicate: Predicate<E>) {
    if (predicate(actual)) return
    expected("$actual did not match what we know")
}