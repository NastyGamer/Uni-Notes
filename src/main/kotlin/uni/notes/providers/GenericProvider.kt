package uni.notes.providers

import org.fife.ui.autocomplete.DefaultCompletionProvider

class GenericProvider : Provider {

    override fun getCompletion() = DefaultCompletionProvider()

}