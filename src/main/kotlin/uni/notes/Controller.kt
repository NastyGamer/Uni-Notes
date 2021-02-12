package uni.notes

import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXTabPane
import com.jfoenix.controls.JFXTreeView
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.Tab
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOUtils
import uni.notes.Notification.showNotification
import uni.notes.Notification.showWarning
import uni.notes.providers.CompletionProviders
import uni.notes.tabs.CodeTab
import uni.notes.tabs.HexTab
import uni.notes.tabs.SettingsTab
import uni.notes.tabs.TerminalTab
import uni.notes.types.File
import java.nio.charset.StandardCharsets
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import javax.swing.JOptionPane
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path


class Controller {

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
    lateinit var buttonPdf: JFXButton

    @FXML
    lateinit var buttonReloadFiletree: JFXButton

    private fun rebuildTree() {
        treeView.root = TreeItem("Notes").also { treeItem -> treeItem.children.addAll(FileTree.subjects) }
    }


    @FXML
    fun initialize() {
        CompletionProviders
        rebuildTree()
        tabPane.tabs.add(CodeTab)
        tabPane.tabs.add(HexTab)
        tabPane.tabs.add(TerminalTab)
        buildFileTree()
        buildButtons()
        setupCrashHandler()
        setupExitHandler()
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

    private fun setupCrashHandler() {
        Thread.setDefaultUncaughtExceptionHandler { _, e ->
            saveCurrentFile()
            CodeTab.currentFile = null
            kotlin.run {
                JOptionPane.showMessageDialog(
                    null,
                    e.stackTraceToString(),
                    "An error occured",
                    JOptionPane.ERROR_MESSAGE
                )
            }
            java.io.File(
                UserPrefs.ROOT_PATH + "/" + "crash-${
                    SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(
                        Timestamp(
                            Date().time
                        )
                    )
                }"
            ).writeText(e.stackTraceToString())
        }
    }

    private fun setupExitHandler() {
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                saveCurrentFile()
            }
        })
    }

    private fun getSelectedFile(): File? {
        return if (treeView.selectionModel.selectedItem == null || treeView.selectionModel.selectedItem !is File) null
        else treeView.selectionModel.selectedItem as File
    }

    private fun saveCurrentFile() {
        CodeTab.currentFile?.save(CodeTab.textArea.text)
    }

    private fun buildButtons() {
        buildNewSubjectButton()
        buildNewNoteButton()
        buildNewCodeButton()
        buildSaveButton()
        buildExternalButton()
        buildReloadButton()
        buildPdfButton()
        buildSettingsButton()
        buildRunButton()
    }

    private fun buildNewSubjectButton() {
        buttonAddSubject.onMouseClicked = EventHandler {
            Input.getSubjectName().let {
                if (it.isNotBlank())
                    FileTree.addSubject(it)
                rebuildTree()
            }
        }
    }

    private fun buildNewNoteButton() {
        buttonAddNote.onMouseClicked = EventHandler {
            Input.getNoteName().let {
                if (it.first != null && it.first!!.isNotBlank() && it.second != null && it.second!!.isNotBlank())
                    FileTree.addNote(it.first!!, it.second!!)
                rebuildTree()
            }
        }
    }

    private fun buildNewCodeButton() {
        buttonAddCode.onMouseClicked = EventHandler {
            Input.getCodeName().let {
                if (it.first != null && it.first!!.isNotBlank() && it.second != null && it.second!!.isNotBlank() && it.third.isNotBlank())
                    FileTree.addFile(it.first!!, it.second!!, it.third)
                rebuildTree()
            }
        }
    }

    private fun buildSaveButton() {
        buttonSave.onMouseClicked = EventHandler { saveCurrentFile() }
    }

    private fun buildReloadButton() {
        buttonReloadFiletree.onMouseClicked = EventHandler { FileTree.refreshTree() }
    }

    @Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
    @OptIn(ExperimentalPathApi::class)
    private fun buildPdfButton() {
        buttonPdf.onMouseClicked = EventHandler {
            getSelectedFile()?.let {
                showNotification("Building ${it.name}", Icons.hammerIcon(), duration = 1000L)
                if (FilenameUtils.isExtension(it.name, "tex")) {
                    saveCurrentFile()
                    val pdfLatexBuilder =
                        ProcessBuilder("pdflatex", (treeView.selectionModel.selectedItem as File).name)
                    pdfLatexBuilder.directory(Path((treeView.selectionModel.selectedItem as File).jFile.absolutePath).parent.toFile())
                    pdfLatexBuilder.redirectErrorStream(true)
                    val pdfLatexProc = pdfLatexBuilder.start()
                    doWhen({ !pdfLatexProc.isAlive }) {
                        kotlin.run {
                            Platform.runLater {
                                TerminalTab.write(
                                    IOUtils.toString(
                                        pdfLatexProc.inputStream,
                                        StandardCharsets.UTF_8
                                    )
                                )
                            }
                            val xdgBuilder = ProcessBuilder(
                                "xdg-open",
                                (treeView.selectionModel.selectedItem as File).name.replace("tex", "pdf")
                            )
                            xdgBuilder.directory(Path((treeView.selectionModel.selectedItem as File).jFile.absolutePath).parent.toFile())
                            xdgBuilder.redirectErrorStream(true)
                            val xdgProc = xdgBuilder.start()
                            doWhen({ !xdgProc.isAlive })
                            { Platform.runLater { TerminalTab.append(IOUtils.toString(xdgProc.inputStream, StandardCharsets.UTF_8)) } }
                        }
                    }
                } else showWarning("File is not a LaTeX source-file")
            }
        }
    }

    private fun buildExternalButton() {
        buttonOpenExternal.onMouseClicked =
            EventHandler {
                getSelectedFile()?.let {
                    showNotification("Opening ${it.name}", Icons.vsCodeIcon())
                    it.openExternally()
                }
            }
    }

    private fun buildSettingsButton() {
        buttonSettings.onMouseClicked =
            EventHandler { tabPane.tabs.add(SettingsTab()); tabPane.selectionModel.selectLast() }
    }

    @Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
    @OptIn(ExperimentalPathApi::class)
    private fun buildRunButton() {
        buttonRunCode.onMouseClicked = EventHandler {
            saveCurrentFile()
            getSelectedFile()?.let {
                showNotification("Building ${it.name}", Icons.hammerIcon(), duration = 1000L)
                if (FilenameUtils.isExtension(it.name, "java")) {
                    val javaBuilder = ProcessBuilder("java", it.name)
                    javaBuilder.directory(Path(it.jFile.absolutePath).parent.toFile())
                    javaBuilder.redirectErrorStream(true)
                    val javaProc = javaBuilder.start()
                    tabPane.selectionModel.select(TerminalTab)
                    doWhen({ !javaProc.isAlive }) {
                        Platform.runLater { TerminalTab.write(IOUtils.toString(javaProc.inputStream, StandardCharsets.UTF_8)) }
                    }
                } else showWarning("File is not a Java source-file")
            }
        }
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