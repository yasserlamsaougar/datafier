package functions

import com.mifmif.common.regex.Generex

class RegexFunctions : AbstractFunctions() {

    val regexMap = mapOf(
            "cloeId" to Generex("1-[0-9A-Z]{5,10}")
    )

    @Generator("id_cloe")
    fun cloeId() : String {
        return regexMap.getValue("cloeId").random()
    }
}