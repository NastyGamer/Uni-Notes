package uni.notes.ui

import javafx.scene.image.Image
import javafx.scene.image.ImageView
import org.apache.commons.io.FilenameUtils
import uni.notes.App

object Icons {

    fun imageIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/tree/file-image-outline.png")))
    fun markdownIcon() =
        ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/tree/language-markdown-outline.png")))

    fun codeIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/buttons/code-tags.png")))
    fun otherIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/tree/file-question.png")))
    fun subjectIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/tree/folder-open.png")))
    fun noteIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/tree/notebook.png")))
    fun settingsIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/buttons/cog.png")))
    fun keyboardIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/other/keyboard.png")))
    fun latexIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/tree/fountain-pen-tip.png")))
    fun htmlIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/tree/language-html5.png")))
    fun pdfIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/tree/file-pdf.png")))
    fun hexIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/other/grid.png")))
    fun terminalIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/other/format-list-bulleted.png")))
    fun hammerIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/notifications/hammer.png")))
    fun vsCodeIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/notifications/microsoft-visual-studio-code.png")))
    fun errorIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/notifications/alert-octagon.png")))
    fun warningIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/notifications/close-octagon.png")))

    operator fun get(fileName: String) = when (FilenameUtils.getExtension(fileName)) {
        "jpeg", "jpg", "png", "gif" -> imageIcon()
        "tex", "bib" -> latexIcon()
        "html", "css", "js", "ts" -> htmlIcon()
        "pdf" -> pdfIcon()
        else -> otherIcon()
    }

}