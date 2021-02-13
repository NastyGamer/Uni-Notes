package uni.notes.ui

import javafx.application.Platform
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uni.notes.App
import java.net.URL

object Notifications {

    private fun showNotification(url: URL, text: String, image: ImageView, duration: Long) {
        val stage = Stage()
        stage.isAlwaysOnTop = true
        stage.isResizable = false
        stage.initStyle(StageStyle.UNDECORATED)
        stage.title = "Notification"
        val root = FXMLLoader(url).load<Parent>()
        val label = root.childrenUnmodifiable[0] as Label
        val imgView = root.childrenUnmodifiable[1] as ImageView
        label.text = text
        imgView.image = image.image
        stage.scene = Scene(root, 300.0, 80.0).also { scene ->
            scene.stylesheets.add(javaClass.classLoader.getResource("style.css")!!.toExternalForm())
        }
        stage.initOwner(App.stage)
        stage.initModality(Modality.NONE)
        stage.x = App.stage.x + App.stage.width - 320
        stage.y = App.stage.y + App.stage.height - 100
        stage.show()
        if (duration > 0) {
            GlobalScope.launch {
                delay(duration)
                Platform.runLater { stage.close() }
            }
        } else {
            stage.scene.onMouseClicked = EventHandler { stage.close() }
        }

    }

    fun showWarning(text: String) {
        showNotification(this.javaClass.classLoader.getResource("fxml/notifications/Warning.fxml")!!, text, Icons.warningIcon(), 3000)
    }

    fun showError(text: String) {
        showNotification(this.javaClass.classLoader.getResource("fxml/notifications/Error.fxml")!!, text, Icons.errorIcon(), -1)
    }

    fun showInfo(text: String, image: ImageView) {
        showNotification(this.javaClass.classLoader.getResource("fxml/notifications/Info.fxml")!!, text, image, 1000)
    }
}