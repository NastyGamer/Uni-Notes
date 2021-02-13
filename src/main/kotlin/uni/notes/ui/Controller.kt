package uni.notes.ui

import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXTabPane
import com.jfoenix.controls.JFXTreeView
import javafx.fxml.FXML
import javafx.scene.control.Tab
import javafx.scene.control.ToggleButton
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import org.apache.commons.io.FilenameUtils
import uni.notes.buttonHandler.*
import uni.notes.io.FileTree
import uni.notes.io.FiletypeAssocs
import uni.notes.providers.CompletionProviders
import uni.notes.tabs.CodeTab
import uni.notes.tabs.HexTab
import uni.notes.tabs.TerminalTab
import uni.notes.types.File
import uni.notes.util.*


class Controller {

    companion object Components {
        lateinit var cHBox: HBox
        lateinit var cToolBar: GridPane
        lateinit var cButtonAddNote: JFXButton
        lateinit var cButtonAddSubject: JFXButton
        lateinit var cButtonRunCode: JFXButton
        lateinit var cButtonAddCode: JFXButton
        lateinit var cButtonSave: JFXButton
        lateinit var cButtonSettings: JFXButton
        lateinit var cTreeView: JFXTreeView<String>
        lateinit var cTabPane: JFXTabPane
        lateinit var cButtonOpenExternal: JFXButton
        lateinit var cButtonPdf: ToggleButton
        lateinit var cButtonReloadFileTree: JFXButton
    }

    @FXML
    lateinit var hbox: HBox

    @FXML
    lateinit var toolBar: GridPane

    @FXML
    lateinit var buttonAddNote: JFXButton

    @FXML
    lateinit var buttonAddSubject: JFXButton

    @FXML
    lateinit var buttonRunCode: JFXButton

    @FXML
    lateinit var buttonAddCode: JFXButton

    @FXML
    lateinit var buttonSave: JFXButton

    @FXML
    lateinit var buttonSettings: JFXButton

    @FXML
    lateinit var treeView: JFXTreeView<String>

    @FXML
    lateinit var tabPane: JFXTabPane

    @FXML
    lateinit var buttonOpenExternal: JFXButton

    @FXML
    lateinit var buttonPdf: ToggleButton

    @FXML
    lateinit var buttonReloadFiletree: JFXButton

    private fun rebuildTree() {
        treeView.root = TreeItem("Notes").also { treeItem -> treeItem.children.addAll(FileTree.subjects) }
    }


    @FXML
    fun initialize() {
        CompletionProviders
        bindComponents()
        rebuildTree()
        tabPane.tabs.addAll(CodeTab, HexTab, TerminalTab)
        buildFileTree()
        buildButtons()
        Thread.setDefaultUncaughtExceptionHandler(CrashHandler())
        Runtime.getRuntime().addShutdownHook(ExitHandler())
    }

    fun onResize(width: Int, height: Int) {
        hbox.prefWidth = width.toDouble()
        hbox.prefHeight = height.toDouble()
        toolBar.prefHeight = height.toDouble()
        treeView.prefWidth = 30 percentOf width
        treeView.prefHeight = height.toDouble()
        tabPane.prefWidth = 70 percentOf width
        tabPane.prefHeight = width.toDouble()
    }

    private fun bindComponents() {
        cHBox = hbox
        cToolBar = toolBar
        cButtonAddNote = buttonAddNote
        cButtonAddSubject = buttonAddSubject
        cButtonRunCode = buttonRunCode
        cButtonAddCode = buttonAddCode
        cButtonSave = buttonSave
        cButtonSettings = buttonSettings
        cTreeView = treeView
        cTabPane = tabPane
        cButtonOpenExternal = buttonOpenExternal
        cButtonPdf = buttonPdf
        cButtonReloadFileTree = buttonReloadFiletree
    }

    private fun buildButtons() {
        NewNoteButtonHandler.setupButton(buttonAddNote)
        NewSubjectButtonHandler.setupButton(buttonAddSubject)
        NewCodeButtonHandler.setupButton(buttonAddCode)
        RunButtonHandler.setupButton(buttonRunCode)
        SaveButtonHandler.setupButton(buttonSave)
        ExternalButtonHandler.setupButton(buttonOpenExternal)
        ReloadButtonHandler.setupButton(buttonReloadFiletree)
        SettingsButtonHandler.setupButton(buttonSettings)
        TogglePreviewButton.setupButton(buttonPdf)
    }

    private fun buildFileTree() {
        treeView.showRootProperty().set(false)
        treeView.selectionModel.selectedItemProperty()
            .addListener { _, _, selectedFile ->
                kotlin.run {
                    if (selectedFile is File) {
                        if (FiletypeAssocs.hasSpecialTab(selectedFile)) {
                            tabPane.tabs.add(
                                FiletypeAssocs[FilenameUtils.getExtension(selectedFile.name)]?.declaredConstructors?.get(
                                    0
                                )?.newInstance(selectedFile) as? Tab
                            )
                            tabPane.selectionModel.selectLast()
                        } else {
                            if (selectedFile.isText()) {
                                CodeTab.openFile(selectedFile)
                                tabPane.selectionModel.select(CodeTab)
                            } else {
                                HexTab.openFile(selectedFile)
                                tabPane.selectionModel.select(HexTab)
                            }

                        }
                    }
                }
            }
    }
}