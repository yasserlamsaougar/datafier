package utilities

import cli.Indexable
import java.nio.file.Files
import java.nio.file.Paths

object DictionaryUtils {

    fun createDictionary(outputPath: String, indexables: List<Indexable>) {
        val bufferedWriter = Files.newBufferedWriter(Paths.get(outputPath))
        bufferedWriter.use {
            indexables.forEach {
                it.index().forEach {
                    bufferedWriter.write(it)
                    bufferedWriter.newLine()
                }
            }
        }
    }
}