package suggestions

import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.search.spell.NGramDistance
import org.apache.lucene.search.spell.PlainTextDictionary
import org.apache.lucene.search.spell.SpellChecker
import org.apache.lucene.store.FSDirectory
import java.nio.file.Paths

class CommandSuggester(dictionaryPath: String) {

    //spell checker instantiation
    val sp = SpellChecker(FSDirectory.open(Paths.get("directory")))

    init {
        //index the dictionary
        sp.stringDistance = NGramDistance()
        sp.indexDictionary(PlainTextDictionary(Paths.get(dictionaryPath)), IndexWriterConfig(), true)
    }

    fun suggest(keyword: String, suggestionNumber: Int = 1): Array<String> {
        if(!sp.exist(keyword)) {
            return sp.suggestSimilar(keyword, suggestionNumber)
        }
        return arrayOf(keyword)
    }

}