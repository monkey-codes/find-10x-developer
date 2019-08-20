package monkey.codes


class PredicateBuilder<E> {

    fun developer(item: E, callback: PredicateItemBuilder<E>.() -> Predicate<E>): Predicate<E> {
        return with(PredicateItemBuilder(item), callback)
    }

    fun whatWeKnow(vararg predicates: Predicate<E>): Predicate<E> {
        return { permutation ->
            predicates.all { match -> match(permutation) }
        }
    }
}

class PredicateItemBuilder<E>(val item: E) {
    val or: Combiner<E> = { left: Predicate<E>, right: Predicate<E>, value: List<E> -> left(value) || right(value) }

    fun theBest(): Predicate<E> = { it.indexOfOrFail(item) == 0 }

    fun isNot(vararg predicates: Predicate<E>): Predicate<E> = { premutation -> !predicates.all { it(premutation) } }

    fun isNot(left: Predicate<E>, combiner: Combiner<E>, right: Predicate<E>): Predicate<E> =
        { permutation -> !combiner(left, right, permutation) }

    fun theWorst(): Predicate<E> = { it.indexOfOrFail(item) == it.size - 1 }

    fun isBetterThan(otherItem: E): Predicate<E> =
        { permutation -> permutation.indexOfOrFail(item) < permutation.indexOfOrFail(otherItem) }

    fun directlyBelow(otherItem: E): Predicate<E> =
        { permutation -> permutation.indexOf(otherItem) + 1 == permutation.indexOf(item) }

    fun directlyAbove(otherItem: E): Predicate<E> =
        { permutation -> permutation.indexOf(otherItem) - 1 == permutation.indexOf(item) }
}

typealias Predicate<E> = (List<E>) -> Boolean

typealias Combiner<E> = (Predicate<E>, Predicate<E>, List<E>) -> Boolean

fun <E> List<E>.indexOfOrFail(item: E) =
    indexOf(item).takeIf { it != -1 } ?: throw IllegalArgumentException("$item not found")