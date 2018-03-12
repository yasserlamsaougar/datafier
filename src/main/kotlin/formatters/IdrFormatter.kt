package formatters
import com.beust.klaxon.JsonObject
import com.beust.klaxon.json
class IdrFormatter : Formatter {

    override fun format(data: Map<String, String>, constants: Map<String, String>): String {
        val time = System.currentTimeMillis()
        val newData = mutableMapOf<String, String>()
        data.forEach {
            newData.set("${it.key.toUpperCase()}_NEW", it.value)
            newData.set("${it.key.toUpperCase()}_OLD", "")
        }
        val operation = "I"
        val map = mapOf(
                "URN" to constants["URN"],
                "TIME" to time,
                "OP" to operation,
                "DATA" to newData
        )
        return json { JsonObject(
                map
        ) }.toJsonString()
    }

}