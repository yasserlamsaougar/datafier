package formatters

interface Formatter {

    fun format(data: Map<String, String>): String

}