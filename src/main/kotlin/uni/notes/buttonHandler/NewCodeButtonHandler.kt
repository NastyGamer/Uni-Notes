package uni.notes.buttonHandler

import com.jfoenix.controls.JFXButton
import javafx.event.EventHandler
import uni.notes.io.FileTree
import uni.notes.io.Input

object NewCodeButtonHandler : ButtonHandler<JFXButton> {

    override fun setupButton(t: JFXButton) {
        t.onMouseClicked = EventHandler {
            Input.getCodeName().let {

                if (it.first != null && it.first!!.isNotBlank() && it.second != null && it.second!!.isNotBlank() && it.third.isNotBlank())
                    FileTree.addFile(it.first!!, it.second!!, it.third)
                FileTree.rebuildTreeView()
            }
        }
    }
}