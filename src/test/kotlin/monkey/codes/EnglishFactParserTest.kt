package monkey.codes

import assertk.assert
import assertk.assertions.containsAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

internal class EnglishFactParserTest {

    lateinit var predicateBuilder: DummyPredicateBuilder

    @BeforeEach
    fun setup() {
        predicateBuilder = DummyPredicateBuilder()
    }

    @Test
    fun `it should verify order based on the best`() {

        EnglishFactParser().parse(
            """|Jessie is not the best developer
               |Evan is the best developer
            """.trimMargin()
        ).invoke(predicateBuilder)

        assert(predicateBuilder.mentionedDevelopers).containsAll("Jessie", "Evan")
        assertTrue (predicateBuilder.orderSatisfiesFacts("Evan", "Jessie"))
    }

    @Test
    fun `it should verify order based on the worst`() {

        EnglishFactParser().parse(
            """|Jessie is not the worst developer
               |Evan is the worst developer
            """.trimMargin()
        ).invoke(predicateBuilder)

        assert(predicateBuilder.mentionedDevelopers).containsAll("Jessie", "Evan")
        assertTrue (predicateBuilder.orderSatisfiesFacts("Jessie", "Evan"))
    }

    @Test
    fun `it should verify order based on the best or the worst`() {

        EnglishFactParser().parse(
            """|John is not the best developer or the worst developer
               |Evan is the best developer
               |Jessie is the worst developer
            """.trimMargin()
        ).invoke(predicateBuilder)

        assert(predicateBuilder.mentionedDevelopers).containsAll("Jessie", "Evan", "John")
        assertTrue (predicateBuilder.orderSatisfiesFacts("Evan", "John", "Jessie"))
    }

    @Test
    fun `it should verify order based on better than`() {

        EnglishFactParser().parse(
            """|Sarah is a better developer than Evan
            """.trimMargin()
        ).invoke(predicateBuilder)

        assert(predicateBuilder.mentionedDevelopers).containsAll("Sarah", "Evan")
        assertTrue (predicateBuilder.orderSatisfiesFacts("Sarah", "Evan"))
    }

    @Test
    fun `it should verify order based on not directly below or above`() {

        EnglishFactParser().parse(
            """|Sarah is not directly below or above John as a developer
               |John is the best developer
               |John is a better developer than Jessie
            """.trimMargin()
        ).invoke(predicateBuilder)

        assert(predicateBuilder.mentionedDevelopers).containsAll("Sarah", "John", "Jessie")
        assertTrue (predicateBuilder.orderSatisfiesFacts("John", "Jessie", "Sarah"))
    }
}

class DummyPredicateBuilder : PredicateBuilder<String> {

    val mentionedDevelopers = mutableSetOf<String>()
    private val predicates = mutableListOf<Predicate<String>>()

    override fun developer(
        item: String,
        developerPredicates: DeveloperPredicateBuilder<String>.() -> Predicate<String>
    ): Predicate<String> {
        return with(DeveloperPredicateBuilder(item) { mentionedDevelopers.add(it) }, developerPredicates)
            .also { predicates.add(it) }
    }

    fun orderSatisfiesFacts(vararg devs: String): Boolean = predicates.all {
        it(devs.toList())
    }
}