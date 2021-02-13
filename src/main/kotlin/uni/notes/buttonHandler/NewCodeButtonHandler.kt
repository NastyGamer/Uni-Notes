package uni.notes.buttonHandler

import com.jfoenix.controls.JFXButton
import javafx.event.EventHandler
import org.apache.commons.io.FilenameUtils
import uni.notes.io.FileTree
import uni.notes.io.Input
import uni.notes.util.all

object NewCodeButtonHandler : ButtonHandler<JFXButton> {

    override fun setupButton(t: JFXButton) {
        t.onMouseClicked = EventHandler {
            Input.getCodeName().let {
                if(it.all { any -> any != null && (any as String).isNotBlank() })
                    FileTree.addFile(it.first!!, it.second!!, it.third)
                FileTree.rebuildTreeView()
            }
        }
    }
}