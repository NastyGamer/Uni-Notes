package uni.notes.buttonHandler

import javafx.event.EventHandler
import javafx.scene.control.ToggleButton

object TogglePreviewButton : ButtonHandler<ToggleButton> {

    override fun setupButton(t: ToggleButton) {
        t.onMouseClicked = EventHandler {
            if (t.isSelected)
                Runtime.getRuntime().exec("evince")
        }
    }
}