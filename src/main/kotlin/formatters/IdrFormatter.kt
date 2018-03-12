package formatters
import com.beust.klaxon.JsonObject
import com.beust.klaxon.json
class IdrFormatter : Formatter {

    override fun format(data: Map<String, String>, constants: Map<String, String>): String {
        val time = System.currentTimeMillis()
        val operation = "I"
        val map = mapOf(
                "URN" to constants["URN"],
                "TIME" to time,
                "OP" to operation,
                "DATA" to data
        )
        return json { JsonObject(
                map
        ) }.toJsonString()
    }

}