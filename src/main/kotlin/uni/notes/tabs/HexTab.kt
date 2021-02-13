package uni.notes.tabs

import javafx.scene.control.Tab
import javafx.scene.control.TextArea
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import uni.notes.ui.Icons
import uni.notes.util.doWhen

object HexTab : Tab() {

    private val bytes = ArrayList<String>()
    private val textArea = TextArea("Open a binary file to see its hex content here")

    fun openFile(file: uni.notes.types.File) {
        textArea.text = "Waiting for indexing to finish..."
        bytes.clear()
        println("Beginning indexing")
        val job = GlobalScope.launch {
            file.jFile.readBytes().forEach { byte -> bytes.add(String.format("%02X ", byte)) }
        }
        doWhen({ job.isCompleted }) { textArea.text = bytes.joinToString(" "); println("Done indexing") }
    }

    init {
        textArea.isEditable = false
        textArea.prefWidth = 1000.0
        textArea.prefHeight = 1000.0
        textArea.isWrapText = true
        textArea.style = "-fx-text-fill: white ;"
        graphic = Icons.hexIcon()
        isClosable = false
        content = textArea
    }
}