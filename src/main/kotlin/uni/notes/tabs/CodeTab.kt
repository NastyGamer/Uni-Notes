package uni.notes.tabs

import javafx.application.Platform
import javafx.embed.swing.SwingNode
import javafx.scene.control.Tab
import org.apache.commons.io.FilenameUtils
import org.fife.ui.autocomplete.AutoCompletion
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import uni.notes.Colors
import uni.notes.Icons
import uni.notes.providers.CompletionProviders
import uni.notes.types.File
import java.awt.Color
import java.awt.Font
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.SwingUtilities

object CodeTab : Tab() {

    val textArea = RSyntaxTextArea()
    var currentFile: File? = null

    private fun setupTextArea() {
        textArea.animateBracketMatching = true
        textArea.antiAliasingEnabled = true
        textArea.closeCurlyBraces = true
        textArea.closeMarkupTags = true
        textArea.eolMarkersVisible = true
        textArea.fractionalFontMetricsEnabled = true
        textArea.isBracketMatchingEnabled = true
        textArea.isAutoIndentEnabled = true
        textArea.isCodeFoldingEnabled = true
        textArea.hyperlinksEnabled = true
        textArea.paintMatchedBracketPair = true
        textArea.paintTabLines = true
        textArea.paintMarkOccurrencesBorder = true
        textArea.background = Colors.grayLight
        textArea.foreground = Color.WHITE
        textArea.currentLineHighlightColor = Colors.grayDark
        textArea.font =
            Font.createFont(Font.TRUETYPE_FONT, javaClass.classLoader.getResourceAsStream("SourceCodePro.ttf"))
                .deriveFont(16f)
        textArea.text = "Open a file to start editing it"
        textArea.isEditable = false
    }

    fun openFile(file: File) {
        currentFile?.save(textArea.text)
        textArea.syntaxEditingStyle = when (FilenameUtils.getExtension(file.name)) {
            "java" -> SyntaxConstants.SYNTAX_STYLE_JAVA
            "tex" -> SyntaxConstants.SYNTAX_STYLE_LATEX
            else -> SyntaxConstants.SYNTAX_STYLE_NONE
        }
        val ac = AutoCompletion(
            when (FilenameUtils.getExtension(file.name)) {
                "java" -> CompletionProviders.javaProvider
                "tex" -> CompletionProviders.latexProvider
                else -> CompletionProviders.genericProvider
            }
        )
        ac.isAutoCompleteEnabled = true
        ac.isAutoActivationEnabled = true
        ac.autoActivationDelay = 0
        ac.install(textArea)
        textArea.isEditable = true
        textArea.text = file.jFile.readText()
        text = file.name
        currentFile = file
    }

    init {
        setupTextArea()
        val node = SwingNode()
        SwingUtilities.invokeLater { node.content = textArea }
        textArea.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                Platform.runLater {
                    while (!node.isFocused) {
                        node.requestFocus()
                    }
                }
            }
        })
        graphic = Icons.keyboardIcon()
        isClosable = false
        content = node
    }
}