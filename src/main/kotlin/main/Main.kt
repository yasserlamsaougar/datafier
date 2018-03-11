package main

import functions.EnumFunctions
import functions.FakerFunctions
import functions.RegexFunctions
import model.Definitions
import model.Definitions.Definition
import model.Definitions.DefinitionTree
import utilities.CombinationUtils
import utilities.ExtractUtils.getPath
import utilities.ExtractUtils.setPath
import utilities.JsonReaderUtil
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

val generators = mapOf(
        "regex" to RegexFunctions(),
        "faker" to FakerFunctions(),
        "enum" to EnumFunctions()
)

fun main(args: Array<String>) {

    val definitionTree = JsonReaderUtil.readFile<DefinitionTree>("src/main/resources/definitions.json")

    val groups = definitionTree.groups

    val result: Map<String, Map<String, List<MutableMap<String, String>>>> = groups.mapValues { (_, v) ->
        val definitions: Map<String, List<MutableMap<String, String>>> = v.objects.mapValues { go ->
            val definition = definitionTree.definitions[go.key]!!
            (0 until go.value.count).map {
                generate(definition)
            }
        }
        v.overrides.forEach { targetKey, expression ->
            resolveOverrides(data = definitions, targetKey = targetKey, expression = expression)
        }
        definitions
    }
    println(result)
    val destructuredRegex = "generate\\s+([a-zA-Z_]+)\\s+as\\s+([a-zA-Z_]+)".toRegex()
    val text = "generate  account as  idr"
    destructuredRegex.matchEntire(text)
            ?.destructured
            ?.let { (groupId, formatId) ->
                println("$groupId as $formatId")
            }
            ?: throw IllegalArgumentException("Bad input '$text'")

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

fun mergeDefinitions(trees: List<DefinitionTree>): DefinitionTree {
    val mergedDefinitions = mutableMapOf<String, Definition>()
    val mergedGroups = mutableMapOf<String, Definitions.Group>()
    trees.forEach {
        mergedDefinitions.putAll(it.definitions)
        mergedGroups.putAll(it.groups)
    }
    return DefinitionTree(definitions = mergedDefinitions, groups = mergedGroups)
}

