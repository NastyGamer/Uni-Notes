package uni.notes.buttonHandler

import com.jfoenix.controls.JFXButton
import javafx.event.EventHandler
import uni.notes.io.FileTree
import uni.notes.io.Input

object NewSubjectButtonHandler : ButtonHandler<JFXButton> {

    override fun setupButton(t: JFXButton) {
        t.onMouseClicked = EventHandler {
            Input.getSubjectName().let {
                if (it.isNotBlank())
                    FileTree.addSubject(it)
                FileTree.rebuildTreeView()
            }
        }
    }
}