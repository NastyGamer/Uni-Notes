package uni.notes.providers

import org.apache.commons.io.FilenameUtils
import org.fife.ui.autocomplete.CompletionProvider

object CompletionProviders {

    val javaProvider: CompletionProvider = JavaProvider().getCompletion()
    val latexProvider: CompletionProvider = LatexProvider().getCompletion()
    val genericProvider: CompletionProvider = GenericProvider().getCompletion()

    operator fun get(fileName: String) = when (FilenameUtils.getExtension(fileName)) {
        "java" -> javaProvider
        "tex", "bib" -> latexProvider
        else -> genericProvider
    }
}