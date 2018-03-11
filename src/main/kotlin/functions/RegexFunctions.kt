package functions

import com.mifmif.common.regex.Generex
import utilities.JsonReaderUtil

class RegexFunctions(regexpsPath: String) : Functions {

    private val functions: Map<String, Generex>

    init {
        val list = JsonReaderUtil.readFile<RegexList>(regexpsPath)
        functions = list.regexps.map {
            it.id to Generex(it.regex)
        }.toMap()

    }

    override fun apply(id: String): String {
        return functions.getValue(id).random()
    }

    data class RegexList(val regexps: List<RegexObject>)
    data class RegexObject(val id: String, val regex: String)
}