package monkey.codes

fun Int.isEven() = this % 2 == 0
fun Int.isOdd() = !isEven()

fun <E> List<E>.permutations(): List<List<E>> =
    mutableListOf<List<E>>().let { accumulator ->
        searchPermutations { accumulator.add(it.toList()) ; false}
        accumulator
    }

fun <E> List<E>.searchPermutations(predicate:(List<E>) -> Boolean): List<E>? {
    val  list = this.toMutableList()
    fun swap(position1: Int, position2: Int) {
        val tmp = list[position1]
        list[position1] = list[position2]
        list[position2] = tmp
    }

    fun heapsAlgorithm(n: Int): List<E>? {
        if(n == 1 && predicate(list)) return list

        for(i in 0 until n) {
            heapsAlgorithm(n -1)?.let { return it }
            when {
                n.isEven() -> swap(i, n - 1)
                n.isOdd() -> swap(0, n - 1)
            }
        }
        return null
    }
    return heapsAlgorithm(list.size)
}