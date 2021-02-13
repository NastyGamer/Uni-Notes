package uni.notes.buttonHandler

import com.jfoenix.controls.JFXButton
import javafx.event.EventHandler
import org.apache.commons.io.FilenameUtils
import uni.notes.io.IO
import uni.notes.tabs.CodeTab
import uni.notes.ui.Controller

object SaveButtonHandler : ButtonHandler<JFXButton> {

    override fun setupButton(t: JFXButton) {
        t.onMouseClicked = EventHandler {
            IO.saveCurrentFile()
            CodeTab.currentFile?.let {
                if (FilenameUtils.isExtension(it.name, "tex") && Controller.cButtonPdf.isSelected)
                    IO.buildAndShowPDF()
            }
        }
    }
}