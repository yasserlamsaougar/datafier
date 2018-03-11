package utilities

object CombinationUtils {

    fun generatePermutations(lists: Array<Collection<String>>): List<String> {
        val mutableList = mutableListOf<String>()
        generatePermutations(lists) {
            mutableList.add(it)
        }
        return mutableList
    }

    fun generatePermutations(lists: Array<Collection<String>>, permutationCallback: (String) -> Unit) {
        generatePermutations(lists, depth = 0, currentItem = "", permutationCallback = permutationCallback)
    }


    private fun generatePermutations(lists: Array<Collection<String>>, depth: Int, currentItem: String, permutationCallback: (String) -> Unit) {
        if (depth == lists.size) {
            permutationCallback(currentItem)
        } else {
            val nextDepth = depth + 1
            lists[depth].forEach {
                val nextItem = "$currentItem $it"
                generatePermutations(lists, depth = nextDepth, currentItem = nextItem, permutationCallback = permutationCallback)
            }
        }

    }


}