package uni.notes.tabs

import javafx.scene.web.WebView
import uni.notes.types.File

class WebTab(file: File) : FiletypeTab(file) {

    init {
        content = WebView().also { webView ->
            webView.engine.load(file.jFile.absolutePath); webView.prefWidth = 1000.0; webView.prefHeight = 1000.0
        }
    }
}