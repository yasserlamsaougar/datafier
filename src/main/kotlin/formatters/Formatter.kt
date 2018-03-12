package formatters

interface Formatter {

    fun format(data: Map<String, String>, constants: Map<String, String>): String

}