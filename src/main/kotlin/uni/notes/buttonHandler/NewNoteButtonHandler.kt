package uni.notes.buttonHandler

import com.jfoenix.controls.JFXButton
import javafx.event.EventHandler
import uni.notes.io.FileTree
import uni.notes.io.Input
import uni.notes.util.all

object NewNoteButtonHandler : ButtonHandler<JFXButton> {

    override fun setupButton(t: JFXButton) {
        t.onMouseClicked = EventHandler {
            Input.getNoteName().let {
                if (it.all { any -> any != null && (any as? String)?.isNotBlank() == true })
                    FileTree.addNote(it.first!!, it.second!!)
                FileTree.rebuildTreeView()
            }
        }
    }
}