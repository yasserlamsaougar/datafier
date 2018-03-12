package utilities

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

object FileUtils {
    fun writeFile(filePath: String, content: String) {
        Files.write(Paths.get(filePath), content.toByteArray(), StandardOpenOption.CREATE)
    }
}