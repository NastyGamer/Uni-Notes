package uni.notes.buttonHandler

import com.jfoenix.controls.JFXButton
import javafx.event.EventHandler
import uni.notes.ui.Controller
import uni.notes.tabs.SettingsTab

object SettingsButtonHandler : ButtonHandler<JFXButton> {

    override fun setupButton(t: JFXButton) {
        t.onMouseClicked = EventHandler {
            Controller.cTabPane.tabs.add(SettingsTab())
            Controller.cTabPane.selectionModel.selectLast()
        }
    }
}