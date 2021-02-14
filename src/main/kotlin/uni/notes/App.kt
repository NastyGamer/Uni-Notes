package uni.notes

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import uni.notes.ui.Controller
import uni.notes.ui.Notifications
import kotlin.math.roundToInt


fun main() {
    Application.launch(App::class.java)
}

class App : Application() {

    companion object Instance {
        lateinit var stage: Stage
    }

    override fun start(stage: Stage) {
        val loader = FXMLLoader(javaClass.classLoader.getResource("fxml/MainView.fxml"))
        val root = loader.load<Parent>()
        val controller = loader.getController<Controller>()
        val scene = Scene(root, 1280.0, 720.0)
        scene.stylesheets.add(javaClass.classLoader.getResource("style.css")!!.toExternalForm())
        stage.title = "FXML Welcome"
        stage.scene = scene
        stage.widthProperty()
            .addListener { _, _, newValue ->
                if (!newValue.toDouble().isNaN() && !stage.height.isNaN()) controller.onResize(
                    newValue.toInt(),
                    stage.height.roundToInt()
                )
            }
        stage.heightProperty()
            .addListener { _, _, newValue ->
                if (!newValue.toDouble().isNaN() && !stage.width.isNaN()) controller.onResize(
                    stage.width.roundToInt(),
                    newValue.toInt()
                )
            }
        stage.xProperty().addListener { _, _, _ -> Notifications.move() }
        stage.yProperty().addListener { _, _, _ -> Notifications.move() }
        stage.icons.add(Image(javaClass.classLoader.getResourceAsStream("Icon.png")))
        stage.title = "Uni Notes"
        Instance.stage = stage
        stage.show()
    }
}