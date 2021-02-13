package uni.notes.types

import javafx.scene.control.TreeItem
import uni.notes.ui.Icons
import uni.notes.util.discard
import java.io.File

data class File(var name: String, var jFile: File) : TreeItem<String>(name, Icons[name]) {

    fun openExternally() = Runtime.getRuntime().exec("code ${jFile.absolutePath}").discard()

    fun save(content: String) {
        jFile.writeText(content)
    }

    fun isText(): Boolean {
        val bytes = jFile.readBytes()
        var ascii = 0
        var nonAscii = 0
        bytes.reduceAsNeeded(500).forEach { byte -> if (isAscii(byte.toInt())) ascii++ else nonAscii++ }
        return if (ascii == 0 && nonAscii == 0) true else (ascii > nonAscii)
    }

    private fun ByteArray.reduceAsNeeded(size: Int): ByteArray {
        if (this.size <= size) return this
        return slice(0 until size).toByteArray()
    }

    private fun isAscii(i: Int) = i in 32..126

}