package cli

import main.generators
import model.Definitions.Definition
import model.Definitions.DefinitionTree
import org.beryx.textio.TextIO
import suggestions.CommandSuggestor
import utilities.CombinationUtils
import utilities.ExtractUtils.getPath
import utilities.ExtractUtils.setPath
import java.util.*

class GenerateCommand(treeDefinition: DefinitionTree, suggestor: CommandSuggestor, val formatters: Map<String, Formatter>) : Command {

    override val treeDefinition = treeDefinition
    override val suggestor = suggestor
    private val destructuringRegex = "generate\\s+([a-zA-Z_]+)\\s+as\\s+([a-zA-Z_]+)".toRegex()

    override fun launch(command: String, textIO: TextIO) {
        val terminal = textIO.textTerminal
        destructuringRegex.matchEntire(command)
                ?.destructured
                ?.let { (groupId, formatId) ->
                    println("$groupId as $formatId")
                }
                ?: terminal.printf("did you mean %s", suggestor.suggest(command, 1))
    }

    override fun index(): List<String> {
        val combinations = arrayOf(
                listOf("generate"),
                treeDefinition.groups.keys,
                listOf("as"),
                formatters.keys
        )
        return CombinationUtils.generatePermutations(combinations)
    }

    fun generate(definition: Definition): MutableMap<String, String> {
        return definition.attributes.mapValues {
            val splitValue = it.value.split('.')
            val randomStrategy = splitValue.first()
            val randomFunction = splitValue.last()
            generators.getValue(randomStrategy).apply(randomFunction)
        }.toMutableMap()
    }

    fun resolveOverrides(data: Map<String, List<MutableMap<String, String>>>, targetKey: String, expression: String) {
        val targetSplit = targetKey.split('.')
        val expressionSplit = expression.split('.')
        val objectKey = targetSplit.first()
        val sourceKey = expressionSplit.first()
        val path = targetSplit.subList(1, targetSplit.size).joinToString(".")
        val sourcePath = expressionSplit.subList(1, expressionSplit.size).joinToString(".")
        val def = data.getValue(objectKey)
        val sourceDef = data.getValue(sourceKey)
        def.forEach {
            it.setPath(path, sourceDef.first().getPath<String>(sourcePath)!!)
        }
    }
}