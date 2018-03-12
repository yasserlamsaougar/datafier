package cli

import formatters.Formatter
import formatters.IdrFormatter
import functions.EnumFunctions
import functions.FakerFunctions
import functions.RegexFunctions
import model.Definitions.DefinitionTree
import org.beryx.textio.TextIoFactory
import suggestions.CommandSuggester
import utilities.DictionaryUtils
import utilities.JsonReaderUtil.readFile

class Datafier {

    val textIO = TextIoFactory.getTextIO()
    val definitionTree = readFile<DefinitionTree>("src/main/resources/definitions.json")
    val generators = mapOf(
            "regex" to RegexFunctions("src/main/resources/regex.json"),
            "faker" to FakerFunctions(),
            "enum" to EnumFunctions("src/main/resources/enums.json")
    )
    val formatList = mapOf<String, Formatter>(
            "idr" to IdrFormatter()
    )
    val commands = mapOf(
            "generate" to GenerateCommand(treeDefinition = definitionTree, formatters = formatList, generators = generators)
    )

    fun start() {
        val dictionaryPath = "src/main/resources/dictionary.txt"
        DictionaryUtils.createDictionary(outputPath = dictionaryPath, indexables = commands.values.toList())
        val commandSuggester = CommandSuggester(dictionaryPath)
        while (true) {
            val command = textIO.newStringInputReader()
                    .read("command")
            val commandSplit = command.split(' ')
            val commandKey = commandSplit.first()
            val commandObject = commands[commandKey]
            if(commandObject != null) {
                if(commandSplit.size == 1) {
                    commandObject.launchInteractive(command, textIO)
                }
                else {
                    commandObject.launch(command, textIO, commandSuggester)
                }
            }
            else {
                val suggestions = commandSuggester.suggest(commandKey, 1)
                if(suggestions.isNotEmpty()) {
                    textIO.textTerminal.println("did you mean ${commandSuggester.suggest(commandKey, 1).first()}")
                }
            }
        }
    }

}

fun main(args: Array<String>) {
    val datafier = Datafier()
    datafier.start()
}

