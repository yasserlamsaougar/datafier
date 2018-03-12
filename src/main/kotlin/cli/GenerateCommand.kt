package cli

import formatters.Formatter
import functions.Functions
import model.Definitions.Definition
import model.Definitions.DefinitionTree
import org.beryx.textio.TextIO
import suggestions.CommandSuggester
import utilities.CombinationUtils
import utilities.ExtractUtils.getPath
import utilities.ExtractUtils.setPath
import utilities.FileUtils

class GenerateCommand(treeDefinition: DefinitionTree, val formatters: Map<String, Formatter>, val generators: Map<String, Functions>) : Command {

    override val treeDefinition = treeDefinition
    private val destructuringRegex = "generate\\s+([a-zA-Z_]+)\\s+as\\s+([a-zA-Z_]+)".toRegex()

    override fun launch(command: String, textIO: TextIO, suggester: CommandSuggester) {
        val terminal = textIO.textTerminal
        destructuringRegex.matchEntire(command)
                ?.destructured
                ?.let { (groupId, formatId) ->
                    val howMany = textIO.newIntInputReader()
                            .withMinVal(1)
                            .withDefaultValue(1)
                            .read("How many ?")
                    val success = generate(groupId, formatId, howMany)
                    if (!success) {
                        terminal.printf("did you mean %s\n", suggester.suggest(command, 1).first())
                    }
                }
                ?: terminal.printf("did you mean %s\n", suggester.suggest(command, 1).first())
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

    private fun generate(groupId: String, formatId: String, count: Int): Boolean {
        val group = treeDefinition.groups[groupId]
        val formatter = formatters[formatId]
        if (group != null && formatter != null) {
            try {
                val content = (0 until count).flatMap {
                    val data = group.objects.map {
                        val definition = treeDefinition.definitions[it.key]!!
                        val generatedData = (0 until it.value.count).map {
                            generate(definition)
                        }
                        it.key to generatedData
                    }.toMap()

                    group.overrides.forEach { targetKey, expression ->
                        resolveOverrides(data = data, targetKey = targetKey, expression = expression)
                    }
                    data.flatMap {
                        val definition = treeDefinition.definitions[it.key]!!
                        it.value.map { e ->
                            formatter.format(e, definition.constants)
                        }
                    }
                }.joinToString("\n")
                FileUtils.writeFile("generated_data/$groupId.json", content)
            } catch (e: Exception) {
                return false
            }
            return true
        }
        return false
    }

    private fun generate(definition: Definition): MutableMap<String, String> {
        return definition.attributes.mapValues {
            val splitValue = it.value.split('.')
            val randomStrategy = splitValue.first()
            val randomFunction = splitValue.last()
            generators.getValue(randomStrategy).apply(randomFunction)
        }.toMutableMap()
    }

    private fun resolveOverrides(data: Map<String, List<MutableMap<String, String>>>, targetKey: String, expression: String) {
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