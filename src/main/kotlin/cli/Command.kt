package cli

import model.Definitions.DefinitionTree
import org.beryx.textio.TextIO
import suggestions.CommandSuggester

interface Command : Indexable {
    val treeDefinition: DefinitionTree

    fun launch(command: String, textIO: TextIO, suggester: CommandSuggester)

    fun launchInteractive(command: String, textIO: TextIO)
}