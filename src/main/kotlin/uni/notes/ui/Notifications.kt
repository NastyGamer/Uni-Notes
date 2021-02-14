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
import uni.notes.util.doWhen
import java.net.URL

object Notifications {

    private val notifications = ArrayList<Notification>()

    private class Notification(fxmlUrl: URL, text: String, image: ImageView, duration: Long) : Stage() {
        init {
            isAlwaysOnTop = true
            isResizable = false
            title = "Notification"
            initStyle(StageStyle.UNDECORATED)
            initOwner(App.stage)
            initModality(Modality.NONE)
            val root = FXMLLoader(fxmlUrl).load<Parent>()
            scene = Scene(root, 300.0, 80.0)
            val label = root.childrenUnmodifiable[0] as Label
            val imgView = root.childrenUnmodifiable[1] as ImageView
            label.text = text
            imgView.image = image.image
            x = App.stage.x + App.stage.width - 320
            y = App.stage.y + App.stage.height - 100
            show()
            update()
            if (duration > 0) {
                GlobalScope.launch {
                    delay(duration)
                    Platform.runLater { close() }
                    notifications.remove(this@Notification)
                    update()
                }
            } else {
                scene.onMouseClicked = EventHandler {
                    close()
                    notifications.remove(this@Notification)
                    update()
                }
            }
        }

    }

    fun move() {
        notifications.forEach { it.x = App.stage.x + App.stage.width - 320 }
        update()
    }

    private fun update() {
        var startY = App.stage.y + App.stage.height - 100
        notifications.forEach { it.y = startY; startY -= 100 }
    }

    fun showWarning(text: String) {
        var notification: Notification? = null
        Platform.runLater {
            notification = Notification(this.javaClass.classLoader.getResource("fxml/notifications/Warning.fxml")!!.toURI().toURL(), text, Icons.warningIcon(), 3000)
        }
        doWhen({ notification != null }) {
            notifications.add(notification!!)
            update()
        }
    }

    fun showError(text: String) {
        var notification: Notification? = null
        Platform.runLater { notification = Notification(this.javaClass.classLoader.getResource("fxml/notifications/Error.fxml")!!.toURI().toURL(), text, Icons.errorIcon(), -1) }
        doWhen({ notification != null }) {
            notifications.add(notification!!)
            update()
        }
    }

    fun showInfo(text: String, image: ImageView) {
        var notification: Notification? = null
        Platform.runLater { notification = Notification(this.javaClass.classLoader.getResource("fxml/notifications/Info.fxml")!!.toURI().toURL(), text, Icons.otherIcon(), 1000) }
        doWhen({ notification != null }) {
            notifications.add(notification!!)
            update()
        }
    }
}