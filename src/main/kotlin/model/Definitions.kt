package model
class Definitions {
    data class Definition(val attributes: Map<String, String> = emptyMap())

    data class GroupObject(val count: Int = 1, val mandatory: Boolean = false)

    data class Group(val objects: Map<String, GroupObject> = emptyMap(), val overrides: Map<String, String> = emptyMap())

    data class DefinitionTree(val definitions: Map<String, Definition>, val groups: Map<String, Group>)
}
