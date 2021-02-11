package uni.notes

import javafx.application.Platform
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

object Notification {

    fun showNotification(text: String, image: ImageView, duration: Long = 2000) {
        val stage = Stage()
        stage.isAlwaysOnTop = true
        stage.isResizable = false
        stage.initStyle(StageStyle.UNDECORATED)
        stage.title = "Notification"
        val root = FXMLLoader(javaClass.classLoader.getResource("Notification.fxml")).load<Parent>()
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
        GlobalScope.launch {
            delay(duration)
            Platform.runLater { stage.close() }
        }
    }

    fun showWarning(text: String) {
        showNotification(text, Icons.alertIcon())
    }
}