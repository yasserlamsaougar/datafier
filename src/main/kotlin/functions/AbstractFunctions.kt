package functions

import org.reflections.ReflectionUtils

abstract class AbstractFunctions : Functions {
    private val functions: Map<String, () -> String>

    init {
        val methods = ReflectionUtils.getAllMethods(this.javaClass, ReflectionUtils.withAnnotation(Generator::class.java))
        functions = methods.map {
            val generatorAnnotation = it.getDeclaredAnnotation(Generator::class.java)
            val id = generatorAnnotation.id
            val function = {
                it.invoke(this) as String
            }
            id to function
        }.toMap()
    }

    override fun apply(id: String): String {
        return functions.getValue(id)()
    }

}