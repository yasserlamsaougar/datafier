package functions

import java.util.*

class EnumFunctions : AbstractFunctions() {

    val enumMap = mapOf(
            "accountLevel" to listOf(
                    "Profil de Facturation",
                    "Tête de Groupe",
                    "Site"
            )
    )

    @Generator("account_level")
    fun accountLevel(): String {
        return enumMap.getValue("accountLevel").random()
    }


    private fun List<String>.random() : String {
        val random = Random()
        return this[random.nextInt(this.size)]
    }
}