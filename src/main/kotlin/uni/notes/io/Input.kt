package uni.notes.io

import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.stage.Modality
import javafx.stage.Stage
import org.apache.commons.io.FilenameUtils
import uni.notes.App
import uni.notes.util.makeValidFilename
import uni.notes.util.makeValidFoldername

object Input {

    fun getSubjectName(): String {
        val stage = Stage()
        val root = FXMLLoader(javaClass.classLoader.getResource("fxml/dialogs/NewSubjectDialog.fxml")).load<Parent>()
        val textField = root.childrenUnmodifiable[0] as TextField
        val cancelButton = (root.childrenUnmodifiable[1] as HBox).children[0]
        val okayButton = (root.childrenUnmodifiable[1] as HBox).children[1]
        stage.scene = Scene(root, 400.0, 200.0).also { scene ->
            scene.stylesheets.add(javaClass.classLoader.getResource("style.css")!!.toExternalForm())
        }
        stage.initOwner(App.stage)
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.isResizable = false
        stage.title = "Create new subject"
        okayButton.onMouseClicked = EventHandler { stage.close() }
        cancelButton.onMouseClicked = EventHandler { stage.close() }
        stage.showAndWait()
        return FilenameUtils.removeExtension(textField.text).makeValidFoldername()
    }

    fun getNoteName(): Pair<String?, String?> {
        val stage = Stage()
        val root = FXMLLoader(javaClass.classLoader.getResource("fxml/dialogs/NewNoteDialog.fxml")).load<Parent>()
        val textField = root.childrenUnmodifiable[0] as TextField
        val cancelButton = (root.childrenUnmodifiable[1] as HBox).children[0]
        val okayButton = (root.childrenUnmodifiable[1] as HBox).children[1]
        val choiceBox = (root.childrenUnmodifiable[2] as ChoiceBox<String>)
        choiceBox.items.addAll(FileTree.subjects.map { subject -> subject.name })
        stage.scene = Scene(root, 400.0, 200.0).also { scene ->
            scene.stylesheets.add(javaClass.classLoader.getResource("style.css")!!.toExternalForm())
        }
        stage.initOwner(App.stage)
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.isResizable = false
        stage.title = "Create new note"
        okayButton.onMouseClicked = EventHandler { stage.close() }
        cancelButton.onMouseClicked = EventHandler { stage.close() }
        stage.showAndWait()
        return Pair(choiceBox.value, FilenameUtils.removeExtension(textField.text).makeValidFoldername())
    }

    fun getCodeName(): Triple<String?, String?, String> {
        val stage = Stage()
        val root = FXMLLoader(javaClass.classLoader.getResource("fxml/dialogs/NewCodeDialog.fxml")).load<Parent>()
        val textField = root.childrenUnmodifiable[0] as TextField
        val cancelButton = (root.childrenUnmodifiable[1] as HBox).children[0]
        val okayButton = (root.childrenUnmodifiable[1] as HBox).children[1]
        @Suppress("UNCHECKED_CAST") val subjectBox = (root.childrenUnmodifiable[3] as ChoiceBox<String>)
        @Suppress("UNCHECKED_CAST") val noteBox = (root.childrenUnmodifiable[2] as ChoiceBox<String>)
        subjectBox.items.addAll(FileTree.subjects.map { subject -> subject.name })
        subjectBox.selectionModel.selectedItemProperty()
            .addListener { _, _, newValue -> noteBox.items.clear(); noteBox.items.addAll(FileTree.subjectByName(newValue).notes.map { note -> FileTree.toString(note) }) }
        stage.scene = Scene(root, 400.0, 200.0).also { scene ->
            scene.stylesheets.add(javaClass.classLoader.getResource("style.css")!!.toExternalForm())
        }
        stage.initOwner(App.stage)
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.isResizable = false
        @Suppress("SpellCheckingInspection")
        stage.title = "Create new Codefile"
        okayButton.onMouseClicked = EventHandler { stage.close() }
        cancelButton.onMouseClicked = EventHandler { stage.close() }
        stage.showAndWait()
        return Triple(subjectBox.value, noteBox.value, (FilenameUtils.removeExtension(textField.text) + ".java").makeValidFilename())
    }
}