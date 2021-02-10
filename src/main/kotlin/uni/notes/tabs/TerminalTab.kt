package uni.notes.tabs

import javafx.scene.control.Tab
import javafx.scene.control.TextArea
import org.apache.commons.lang3.StringUtils
import uni.notes.Icons

object TerminalTab : Tab() {

    private val textArea = TextArea("Output will appear here")

    fun write(text: String) {
        textArea.text = text
        this.text = StringUtils.abbreviate(textArea.text, 5)
    }

    fun append(text: String) {
        textArea.text = "\n" + textArea.text + text
        this.text = StringUtils.abbreviate(textArea.text, 5)
    }

    fun clear() {
        textArea.text = "Output will appear here"
        this.text = ""
    }

    init {
        textArea.isEditable = false
        textArea.prefWidth = 1000.0
        textArea.prefHeight = 1000.0
        textArea.isWrapText = true
        textArea.style = "-fx-text-fill: white ;"
        graphic = Icons.terminalIcon()
        isClosable = false
        content = textArea
    }

}