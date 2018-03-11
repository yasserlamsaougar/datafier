package utilities

import com.beust.klaxon.Klaxon
import java.io.File

object JsonReaderUtil {
    val klaxon = Klaxon()

    inline fun <reified T> readFile(path: String): T {
        return klaxon.parse(File(path))!!
    }

    inline fun <reified T> readDir(path: String): List<T> {
        return File(path).walkTopDown().filter { it.isFile }.map {
            klaxon.parse<T>(it.absoluteFile)!!
        }.toList()
    }
}