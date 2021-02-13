package uni.notes.buttonHandler

import javafx.scene.control.ButtonBase

interface ButtonHandler<T : ButtonBase> {

    fun setupButton(t: T)

}