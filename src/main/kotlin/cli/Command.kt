package cli

import model.Definitions.DefinitionTree
import org.beryx.textio.TextIO
import suggestions.CommandSuggestor

interface Command : Indexable {
    val treeDefinition: DefinitionTree
    val suggestor: CommandSuggestor

    fun launch(command: String, textIO: TextIO)
}