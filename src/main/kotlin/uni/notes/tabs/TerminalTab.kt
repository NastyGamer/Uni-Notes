package uni.notes.tabs

import javafx.scene.control.Tab
import javafx.scene.control.TextArea
import org.apache.commons.lang3.StringUtils
import uni.notes.ui.Icons

object TerminalTab : Tab() {

    private val textArea = TextArea("Output will appear here")

    fun write(text: String) {
        textArea.text = text
        this.text = makeTitle()
    }

    fun append(text: String) {
        textArea.text = "\n" + textArea.text + text
        this.text = makeTitle()
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

    private fun makeTitle() = StringUtils.abbreviate(textArea.text.replace("\n", ""), 15)
}