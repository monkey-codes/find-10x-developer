package monkey.codes

interface PredicateBuilder<E> {
    fun developer(item: E, developerPredicates: DeveloperPredicateBuilder<E>.() -> Predicate<E>): Predicate<E>
}

class DeveloperRanker<E>(facts: PredicateBuilder<E>.() -> Unit) : PredicateBuilder<E> {
    private val predicates = mutableListOf<Predicate<E>>()
    private val discoveredDevelopers = mutableSetOf<E>()

    init {
        apply(facts)
    }

    fun rank() = discoveredDevelopers.toList().searchPermutations(whatWeKnow())?.let { Ranking(it) }

    override fun developer(
        item: E,
        developerPredicates: DeveloperPredicateBuilder<E>.() -> Predicate<E>
    ) = with(DeveloperPredicateBuilder(item) { discoveredDevelopers.add(it) }, developerPredicates)
            .also { predicates.add(it) }

    private fun whatWeKnow(): Predicate<E> {
        return { permutation ->
            predicates.all { match -> match(permutation) }
        }
    }
}

class DeveloperPredicateBuilder<E>(val developer: E, val registerDeveloper: (E) -> Unit) {

    val register: (E, Predicate<E>) -> Predicate<E> = { item, prediate ->
        registerDeveloper(item)
        prediate
    }

    init {
        registerDeveloper(developer)
    }

    val or: Combiner<E> = { left: Predicate<E>, right: Predicate<E>, value: List<E> -> left(value) || right(value) }

    fun theBest(): Predicate<E> = { it.indexOf(developer) == 0 }

    fun `is`(predicate: Predicate<E>) = predicate

    fun isNot(vararg predicates: Predicate<E>): Predicate<E> = { permutation -> !predicates.all { it(permutation) } }

    fun isNot(left: Predicate<E>, combiner: Combiner<E>, right: Predicate<E>): Predicate<E> =
        { permutation -> !combiner(left, right, permutation) }

    fun theWorst(): Predicate<E> = { it.indexOf(developer) == it.size - 1 }

    fun isBetterThan(otherDeveloper: E): Predicate<E> =
        register(otherDeveloper) { permutation -> permutation.indexOf(developer) < permutation.indexOf(otherDeveloper) }

    fun directlyBelow(otherDeveloper: E): Predicate<E> =
        register(otherDeveloper) { permutation ->
            permutation.indexOf(otherDeveloper) + 1 == permutation.indexOf(developer)
        }

    fun directlyAbove(otherDeveloper: E): Predicate<E> =
        register(otherDeveloper) { permutation ->
            permutation.indexOf(otherDeveloper) - 1 == permutation.indexOf(developer)
        }
}

typealias Predicate<E> = (List<E>) -> Boolean

typealias Combiner<E> = (Predicate<E>, Predicate<E>, List<E>) -> Boolean

data class Ranking<E>(val order: List<E>) {
    val tenXDeveloper: E
        get() = order.first()
}
