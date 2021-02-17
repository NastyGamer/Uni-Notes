package uni.notes.tabs

import javafx.scene.control.Tab
import javafx.scene.control.TextArea
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.apache.commons.io.IOUtils
import uni.notes.ui.Icons
import uni.notes.util.Logger
import uni.notes.util.doWhen
import java.nio.charset.StandardCharsets

object HexTab : Tab() {

    private val textArea = TextArea("Open a binary file to see its hex content here")

    fun openFile(file: uni.notes.types.File) {
        textArea.text = "Waiting for indexing to finish.\nThis may take a while..."
        val builder = ProcessBuilder("xxd", "-u", file.jFile.absolutePath)
        builder.redirectErrorStream(true)
        val process = builder.start()
        GlobalScope.launch {
            textArea.clear()
            @Suppress("BlockingMethodInNonBlockingContext")
            while(process.isAlive)
                textArea.text += IOUtils.toString(process.inputStream, StandardCharsets.UTF_8)
        }
    }

    init {
        textArea.isEditable = false
        textArea.prefWidth = 1000.0
        textArea.prefHeight = 1000.0
        textArea.isWrapText = true
        textArea.style = "-fx-text-fill: white"
        graphic = Icons.hexIcon()
        isClosable = false
        content = textArea
    }
}