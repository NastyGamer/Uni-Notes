package uni.notes.types

import javafx.scene.control.TreeItem
import org.apache.commons.io.FilenameUtils
import uni.notes.Icons
import uni.notes.discard
import java.io.File

data class File(var name: String, var jFile: File) : TreeItem<String>(
    name, when (FilenameUtils.getExtension(name)) {
        "md" -> Icons.markdownIcon()
        "java" -> Icons.codeIcon()
        "png", "jpg", "jpeg" -> Icons.imageIcon()
        "tex", "bst", "bbl", "blg", "brf", "cls", "dtx", "aux" -> Icons.latexIcon()
        "html", "css", "js", "ts" -> Icons.htmlIcon()
        "pdf" -> Icons.pdfIcon()
        else -> Icons.otherIcon()
    }
) {

    fun openExternally() = Runtime.getRuntime().exec("code ${jFile.absolutePath}").discard()

    fun save(content: String) {
        jFile.writeText(content)
    }

    fun isText(): Boolean {
        val bytes = jFile.readBytes()
        var ascii = 0
        var nonAscii = 0
        bytes.reduceAsNeeded(500).forEach { byte -> if (isAscii(byte.toInt())) ascii++ else nonAscii++ }
        println("Analysis result: Ascii: $ascii Non-Ascii: $nonAscii => ${if (ascii > nonAscii) "Ascii" else "Non-Ascii"}")
        return if (ascii == 0 && nonAscii == 0) true else (ascii > nonAscii)
    }

    private fun ByteArray.reduceAsNeeded(size: Int): ByteArray {
        if (this.size <= size) return this
        return slice(0 until size).toByteArray()
    }

    private fun isAscii(i: Int) = i in 32..126

}