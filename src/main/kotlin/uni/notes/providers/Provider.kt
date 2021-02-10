package uni.notes.providers

import org.fife.ui.autocomplete.CompletionProvider

interface Provider {

    fun getCompletion(): CompletionProvider

}