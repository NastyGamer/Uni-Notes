package uni.notes

import javafx.scene.image.Image
import javafx.scene.image.ImageView

object Icons {

    fun imageIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/file-image-outline.png")))
    fun markdownIcon() =
        ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/language-markdown-outline.png")))

    fun codeIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/code-tags.png")))
    fun otherIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/file-question.png")))
    fun subjectIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/folder-open.png")))
    fun noteIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/notebook.png")))
    fun settingsIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/cog.png")))
    fun keyboardIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/keyboard.png")))
    fun latexIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/fountain-pen-tip.png")))
    fun htmlIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/language-html5.png")))
    fun pdfIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/file-pdf.png")))
    fun hexIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/grid.png")))
    fun terminalIcon() = ImageView(Image(App::class.java.classLoader.getResourceAsStream("icons/format-list-bulleted.png")))
}