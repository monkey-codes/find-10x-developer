package monkey.codes

class DeveloperRank<E>(facts: PredicateBuilder<E>.() -> Unit) {
    private val predicate: Predicate<E>

    private val items: List<E>

    init {
        val predicateBuilder = DefaultPredicateBuilder<E>()
        predicate = predicateBuilder.whatWeKnow(facts)
        items = predicateBuilder.items()
    }

    val order: List<E>? by lazy {
        items.permutations(predicate)
    }

    val tenXDeveloper: E? by lazy {
        order?.first()
    }
}

interface PredicateBuilder<E> {
    fun developer(item: E, callback: PredicateItemBuilder<E>.() -> Predicate<E>): Predicate<E>
}

private class DefaultPredicateBuilder<E> : PredicateBuilder<E> {

    private val predicates = mutableListOf<Predicate<E>>()
    private val discoveredItems = mutableSetOf<E>()

    override fun developer(item: E, callback: PredicateItemBuilder<E>.() -> Predicate<E>): Predicate<E> {
        return with(PredicateItemBuilder(item) { discoveredItems.add(it) }, callback)
            .also { predicates.add(it) }
    }

    fun whatWeKnow(callback: PredicateBuilder<E>.() -> Unit): Predicate<E> {
        with(this, callback)
        return { permutation ->
            predicates.all { match -> match(permutation) }
        }
    }

    fun items() = discoveredItems.toList()
}

class PredicateItemBuilder<E>(val item: E, val registerItem: (E) -> Unit) {

    val register: (E, Predicate<E>) -> Predicate<E> = { item, prediate ->
        registerItem(item)
        prediate
    }

    init {
        registerItem(item)
    }

    val or: Combiner<E> = { left: Predicate<E>, right: Predicate<E>, value: List<E> -> left(value) || right(value) }

    fun theBest(): Predicate<E> = { it.indexOfOrFail(item) == 0 }

    fun isNot(vararg predicates: Predicate<E>): Predicate<E> = { premutation -> !predicates.all { it(premutation) } }

    fun isNot(left: Predicate<E>, combiner: Combiner<E>, right: Predicate<E>): Predicate<E> =
        { permutation -> !combiner(left, right, permutation) }

    fun theWorst(): Predicate<E> = { it.indexOfOrFail(item) == it.size - 1 }

    fun isBetterThan(otherItem: E): Predicate<E> =
        register(otherItem) { permutation -> permutation.indexOfOrFail(item) < permutation.indexOfOrFail(otherItem) }

    fun directlyBelow(otherItem: E): Predicate<E> =
        register(otherItem) { permutation -> permutation.indexOf(otherItem) + 1 == permutation.indexOf(item) }

    fun directlyAbove(otherItem: E): Predicate<E> =
        register(otherItem) { permutation -> permutation.indexOf(otherItem) - 1 == permutation.indexOf(item) }
}

typealias Predicate<E> = (List<E>) -> Boolean

typealias Combiner<E> = (Predicate<E>, Predicate<E>, List<E>) -> Boolean

fun <E> List<E>.indexOfOrFail(item: E) =
    indexOf(item).takeIf { it != -1 } ?: throw IllegalArgumentException("$item not found")