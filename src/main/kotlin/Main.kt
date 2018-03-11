import com.beust.klaxon.Klaxon
import functions.EnumFunctions
import functions.FakerFunctions
import functions.Functions
import functions.RegexFunctions
import java.io.File
import ExtractUtils.getPath
import ExtractUtils.setPath

val generators = mapOf<String, Functions>(
        "regex" to RegexFunctions(),
        "faker" to FakerFunctions(),
        "enum" to EnumFunctions()
)

fun main(args: Array<String>) {

    val klaxon = Klaxon()
    val definitionTree = klaxon.parse<DefinitionTree>(File("src/main/resources/definitions.json"))!!


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

data class Definition(val attributes: Map<String, String> = emptyMap())

data class GroupObject(val count: Int = 1, val mandatory: Boolean = false)

data class Group(val objects: Map<String, GroupObject> = emptyMap(), val overrides: Map<String, String> = emptyMap())

data class DefinitionTree(val definitions: Map<String, Definition>, val groups: Map<String, Group>)