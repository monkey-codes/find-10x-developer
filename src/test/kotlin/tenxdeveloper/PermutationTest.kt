package tenxdeveloper

import assertk.all
import assertk.assert
import assertk.assertions.containsAll
import assertk.assertions.hasSize
import assertk.assertions.isFalse
import org.junit.Test
import java.util.stream.IntStream


class PermutationTest {

    @Test
    fun `it should generate permutations`(){
        val range = (1..3)
        val allPermutations = range.toList().permutations()
        assert(allPermutations).all {
            hasSize(factorial(range.last))
            containsAll(
                listOf(1,2,3),
                listOf(2,1,3),
                listOf(3,2,1),
                listOf(1,3,2),
                listOf(2,3,1),
                listOf(3,2,1)
            )
        }
    }

    @Test
    fun `an empty list should have zero permutations`(){
        val allPermutations = emptyList<Int>().toList().permutations()
        assert(allPermutations).hasSize(0)
    }


    @Test
    fun `a list with 1 item should have 1 permutation`(){
        val allPermutations = listOf(1).toList().permutations()
        assert(allPermutations).hasSize(1)
    }

    @Test
    fun `it should stop generating permutations once a match is found`(){
        val range = (1..3)
        var found = false
        val match = range.toList().searchPermutations{
            assert(found).isFalse()
            found = it.equals(listOf(1,2,3))
            found
        }
    }

    private fun factorial(n: Int) = IntStream.rangeClosed(1, n).reduce(1) { x, y -> x * y}
}