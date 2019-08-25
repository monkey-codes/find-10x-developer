package tenxdeveloper

import java.io.File
import java.io.FileReader

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            args
                .firstOrNull { File(it).exists() }
                ?.let {
                    val facts = FileReader(it).readText()
                    val ranker = DeveloperRanker(
                        facts = EnglishFactParser().parse(facts)
                    )
                    ranker.rank()?.let {
                        println("Order: ${it.order}")
                        println("10xDeveloper: ${it.tenXDeveloper}")
                    } ?: println("Could not rank based on given facts:\n$facts")
                } ?: println("Could not read input file. args: ${args.joinToString { it }}")
        }
    }
}
