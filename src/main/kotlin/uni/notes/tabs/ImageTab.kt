package uni.notes.tabs

import javafx.scene.control.ScrollPane
import javafx.scene.image.ImageView
import uni.notes.types.File

open class ImageTab(file: File) : FiletypeTab(file) {

    init {
        content = ScrollPane(ImageView(file.jFile.toURI().toURL().toString()))
    }
}