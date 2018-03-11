package cli

import model.Definitions.DefinitionTree
import org.beryx.textio.TextIoFactory
import utilities.JsonReaderUtil.readFile

class Datafier() {

    val textIO = TextIoFactory.getTextIO()
    val definitionTree = readFile<DefinitionTree>("src/main/resources/definitions.json")

    fun start() {
        while (true) {
            val command = textIO.newStringInputReader()
                    .read("command")

        }
    }

}

