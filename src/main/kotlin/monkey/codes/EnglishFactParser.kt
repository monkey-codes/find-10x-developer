package monkey.codes


class EnglishFactParser {
    private val recognizablePatterns = listOf(
        FactParser("""(\w+) is not the best developer""") { (name) ->
            { developer(name) { isNot(theBest()) } }
        },
        FactParser("""(\w+) is the best developer""") { (name) ->
            { developer(name) { `is`(theBest()) } }
        },
        FactParser("""(\w+) is not the worst developer""") { (name) ->
            { developer(name) { isNot(theWorst()) } }
        },
        FactParser("""(\w+) is the worst developer""") { (name) ->
            { developer(name) { `is`(theWorst()) } }
        },
        FactParser("""(\w+) is not the best developer or the worst developer""") { (name) ->
            { developer(name) { isNot(theBest(), or, theWorst()) } }
        },
        FactParser("""(\w+) is a better developer than (\w+)""") { (name1, name2) ->
            { developer(name1) { isBetterThan(name2) } }
        },
        FactParser("""(\w+) is a better developer than (\w+)""") { (name1, name2) ->
            { developer(name1) { isBetterThan(name2) } }
        },
        FactParser("""(\w+) is not directly below or above (\w+) as a developer""") { (name1, name2) ->
            { developer(name1) { isNot(directlyBelow(name2), or, directlyAbove( name2)) } }
        }
    )

    fun parse(facts: String): PredicateBuilder<String>.() -> Unit {
        return {
            facts.lines().forEach { line ->
                recognizablePatterns
                    .firstOrNull { it.matches(line) }
                    ?.let {
                        apply(it.addFacts(line))
                    }
            }
        }
    }
}

class FactParser(
    pattern: String,
    val factsBuilder: (MatchResult.Destructured) -> PredicateBuilder<String>.() -> Unit
) {
    private val regex = pattern.toRegex()
    fun matches(line: String) = regex.matches(line)
    fun addFacts(line: String) = factsBuilder(regex.find(line)!!.destructured)
}