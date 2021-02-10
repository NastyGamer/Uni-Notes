package uni.notes.types

import javafx.scene.control.TreeItem
import uni.notes.Icons

class Note(val name: String, val files: ArrayList<File>) : TreeItem<String>(name, Icons.noteIcon()) {

    init {
        children.addAll(files)
    }

    fun addFile(file: File) = files.add(file)

    override fun toString(): String {
        return "Note(name='$name', files=$files)"
    }
}