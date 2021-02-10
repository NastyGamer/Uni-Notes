package uni.notes.tabs

import javafx.scene.control.Tab
import uni.notes.types.File

open class FiletypeTab(file: File) : Tab() {

    init {
        text = file.name
        isClosable = true
        graphic = file.graphic
    }

}