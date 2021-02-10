package uni.notes.providers

import org.fife.ui.autocomplete.CompletionProvider

object CompletionProviders {

    val javaProvider: CompletionProvider = JavaProvider().getCompletion()
    val latexProvider: CompletionProvider = LatexProvider().getCompletion()
    val genericProvider: CompletionProvider = GenericProvider().getCompletion()
}