package uni.notes.buttonHandler

import com.jfoenix.controls.JFXButton
import javafx.event.EventHandler
import uni.notes.io.IO
import uni.notes.ui.Controller
import uni.notes.ui.Icons
import uni.notes.ui.Notifications
import uni.notes.util.getSelectedFile

object ExternalButtonHandler : ButtonHandler<JFXButton> {

    override fun setupButton(t: JFXButton) {
        t.onMouseClicked = EventHandler {
            Controller.cTreeView.getSelectedFile()?.let {
                Notifications.showInfo("Opening ${it.name}", Icons.vsCodeIcon())
                IO.openCurrentFileExternally()
            }
        }
    }
}