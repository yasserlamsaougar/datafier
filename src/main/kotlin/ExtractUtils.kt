object ExtractUtils {
    fun <T> Map<String, Any>.getPath(path: String): T? {
        val split = path.split('.')
        if (split.isEmpty()) {
            throw IllegalArgumentException("the path must not be empty")
        } else {
            var result: Any? = this
            split.forEach {
                result = extractValue(result, it)
            }
            return result as? T
        }
    }

    fun MutableMap<String, *>.setPath(path: String, value: Any) {
        val split = path.split('.')
        if (split.isEmpty()) {
            throw IllegalArgumentException("the path must not be empty")
        } else {
            var result: Any? = this
            val keyToSet = split.last()
            split.subList(0, split.size - 1).forEach {
                result = extractValue(result, it)
            }
            when (result) {
                is MutableMap<*, *> -> (result as MutableMap<String, Any?>).set(keyToSet, value)
                is MutableList<*> -> (result as MutableList<Any>).set(keyToSet.toInt(), value)
                else -> throw IllegalArgumentException("can't set value of a incorrect path $path")
            }

        }
    }

    private fun extractValue(target: Any?, currentKey: String): Any? {
        if (target == null) {
            throw IllegalArgumentException("object referenced by $currentKey is null")
        } else {
            return when (target) {
                is Map<*, *>  -> (target as Map<String, Any?>)[currentKey]
                is List<Any?> -> target[currentKey.toInt()]
                is Array<*> -> (target as Array<Any?>)[currentKey.toInt()]
                else -> throw IllegalArgumentException("current key $currentKey pointing to wrong object type ${target.javaClass.canonicalName}")
            }
        }
    }

}