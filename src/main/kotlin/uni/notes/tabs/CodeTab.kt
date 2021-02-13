package uni.notes.tabs

import javafx.application.Platform
import javafx.embed.swing.SwingNode
import javafx.scene.control.Tab
import org.apache.commons.io.FilenameUtils
import org.fife.ui.autocomplete.AutoCompletion
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rtextarea.RTextScrollPane
import uni.notes.io.IO
import uni.notes.providers.CompletionProviders
import uni.notes.types.File
import uni.notes.ui.Colors
import uni.notes.ui.Icons
import uni.notes.util.SyntaxHighlighter
import java.awt.Color
import java.awt.Font
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.SwingUtilities

object CodeTab : Tab() {

    val textArea = RSyntaxTextArea()
    private val scrollPane = RTextScrollPane(textArea)
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
        textArea.tabSize = 4
        textArea.font =
            Font.createFont(Font.TRUETYPE_FONT, javaClass.classLoader.getResourceAsStream("SourceCodePro.ttf"))
                .deriveFont(16f)
        textArea.text = "Open a file to start editing it"
        textArea.isEditable = false
        scrollPane.lineNumbersEnabled = true
        scrollPane.isFoldIndicatorEnabled = true
        scrollPane.isIconRowHeaderEnabled = true
        scrollPane.background = Colors.grayDark
        scrollPane.horizontalScrollBar.background = Colors.grayDark
    }

    fun openFile(file: File) {
        currentFile?.save(textArea.text)
        textArea.restoreDefaultSyntaxScheme()
        textArea.syntaxEditingStyle = SyntaxHighlighter[file.name]
        val ac = AutoCompletion(CompletionProviders[file.name])
        ac.isAutoCompleteEnabled = true
        ac.isAutoActivationEnabled = true
        ac.autoActivationDelay = 0
        ac.showDescWindow = true
        ac.install(textArea)
        textArea.isEditable = true
        textArea.text = file.jFile.readText()
        text = file.name
        currentFile = file
    }

    fun reloadFile() {
        currentFile?.let { textArea.text = it.jFile.readText() }
    }

    init {
        setupTextArea()
        val node = SwingNode()
        SwingUtilities.invokeLater { node.content = scrollPane }
        textArea.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                Platform.runLater {
                    while (!node.isFocused) {
                        node.requestFocus()
                    }
                }
            }
        })
        textArea.addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent) {
                if (e.keyCode == KeyEvent.VK_S && e.isControlDown) {
                    currentFile?.save(textArea.text)
                    if (FilenameUtils.isExtension(currentFile?.name, "tex")) {
                        IO.buildAndShowPDF()
                    }
                }
            }
        })
        graphic = Icons.keyboardIcon()
        isClosable = false
        content = node
    }
}