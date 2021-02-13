package uni.notes.buttonHandler

import com.jfoenix.controls.JFXButton
import javafx.event.EventHandler
import uni.notes.io.FileTree

object ReloadButtonHandler : ButtonHandler<JFXButton> {

    override fun setupButton(t: JFXButton) {
        t.onMouseClicked = EventHandler { FileTree.rebuildTreeView() }
    }
}