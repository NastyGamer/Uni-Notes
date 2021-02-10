package uni.notes.types

import javafx.scene.control.TreeItem
import uni.notes.Icons

class Subject(val name: String, val notes: ArrayList<Note>) : TreeItem<String>(name, Icons.subjectIcon()) {

    init {
        children.addAll(notes)
    }

    fun addNote(note: Note) = notes.add(note)

    override fun toString(): String {
        return "Subject(name='$name', notes=$notes)"
    }
}