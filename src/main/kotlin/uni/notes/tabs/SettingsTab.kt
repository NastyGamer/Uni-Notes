package uni.notes.tabs

import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.Tab
import javafx.scene.control.TextField
import javafx.scene.layout.*
import javafx.stage.DirectoryChooser
import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.DirectoryFileFilter
import org.apache.commons.validator.routines.UrlValidator
import uni.notes.*
import java.io.File
import java.io.FileFilter
import kotlin.system.exitProcess

class SettingsTab : Tab() {

    init {
        graphic = Icons.settingsIcon()
        text = "Settings"
        val pathField = TextField(UserPrefs.NOTEBOOK_PATH)
        val changePathButton = Button(String(), Icons.noteIcon())
        val templatePathField = TextField(UserPrefs.TEMPLATE_URL)
        val templateConfirmButton = Button("Apply")
        pathField.background = Background(BackgroundFill(Colors.grayLight.asPaint(), CornerRadii.EMPTY, Insets.EMPTY))
        changePathButton.background =
            Background(BackgroundFill(Colors.grayLight.asPaint(), CornerRadii.EMPTY, Insets.EMPTY))
        templatePathField.background =
            Background(BackgroundFill(Colors.grayLight.asPaint(), CornerRadii.EMPTY, Insets.EMPTY))
        templateConfirmButton.background =
            Background(BackgroundFill(Colors.grayLight.asPaint(), CornerRadii.EMPTY, Insets.EMPTY))
        pathField.style = "-fx-text-inner-color: white"
        changePathButton.style = "-fx-text-inner-color: white"
        templatePathField.style = "-fx-text-inner-color: white"
        templateConfirmButton.style = "-fx-text-inner-color: white"
        templateConfirmButton.onMouseClicked = EventHandler {
            if (UrlValidator(arrayOf("http", "https")).isValid(templatePathField.text))
                UserPrefs.TEMPLATE_URL = templatePathField.text
        }
        changePathButton.onMouseClicked = EventHandler {
            val dir = DirectoryChooser().showDialog(App.stage)
            pathField.text = dir.absolutePath
            if (dir.absolutePath != UserPrefs.NOTEBOOK_PATH) {
                File(UserPrefs.NOTEBOOK_PATH).listFiles(DirectoryFileFilter.INSTANCE as FileFilter)!!
                    .forEach { folder ->
                        FileUtils.moveDirectoryToDirectory(folder, dir, true)
                    }
                UserPrefs.NOTEBOOK_PATH = dir.absolutePath
                exitProcess(0)
            }
        }
        content = VBox(HBox(pathField, changePathButton).also { box -> box.padding = Insets(20.0) },
            HBox(templatePathField, templateConfirmButton).also { box ->
                box.padding = Insets(20.0)
            }).also { box -> box.padding = Insets(20.0) }
    }
}