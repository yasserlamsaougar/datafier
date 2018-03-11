package functions

import utilities.JsonReaderUtil
import java.util.*

class EnumFunctions(enumPath: String) : Functions {

    private val functions: Map<String, () -> String>
    init {
        val list = JsonReaderUtil.readFile<EnumObjectList>(enumPath)
        functions = list.enums.map {
            it.id to {
                it.values.random()
            }
        }.toMap()

    }
    override fun apply(id: String): String {
        return functions.getValue(id)()
    }


    private fun List<String>.random() : String {
        val random = Random()
        return this[random.nextInt(this.size)]
    }
    data class EnumObjectList(val enums: List<EnumObject>)
    data class EnumObject(val id: String, val values: List<String>)
}