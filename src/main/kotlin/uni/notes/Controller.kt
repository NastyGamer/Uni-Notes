package uni.notes

import com.jfoenix.controls.JFXTabPane
import com.jfoenix.controls.JFXTreeView
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Tab
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOUtils
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
    lateinit var buttonAddNote: Button

    @FXML
    lateinit var buttonAddSubject: Button

    @FXML
    lateinit var buttonRunCode: Button

    @FXML
    lateinit var buttonAddCode: Button

    @FXML
    lateinit var buttonSave: Button

    @FXML
    lateinit var buttonSettings: Button

    @FXML
    lateinit var treeView: JFXTreeView<String>

    @FXML
    lateinit var tabPane: JFXTabPane

    @FXML
    lateinit var buttonOpenExternal: Button

    @FXML
    lateinit var buttonPdf: Button

    @FXML
    lateinit var buttonReloadFiletree: Button

    private fun rebuildTree() {
        treeView.root = TreeItem("Notes").also { treeItem -> treeItem.children.addAll(FileTree.subjects) }
    }

    @Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
    @OptIn(ExperimentalPathApi::class)
    @FXML
    fun initialize() {
        CompletionProviders
        rebuildTree()
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
        tabPane.tabs.add(CodeTab)
        tabPane.tabs.add(HexTab)
        tabPane.tabs.add(TerminalTab)
        buttonSettings.onMouseClicked =
            EventHandler { tabPane.tabs.add(SettingsTab()); tabPane.selectionModel.selectLast() }
        buttonAddSubject.onMouseClicked = EventHandler {
            Input.getSubjectName().let {
                if (it.isNotBlank())
                    FileTree.addSubject(it)
                rebuildTree()
            }
        }
        buttonAddNote.onMouseClicked = EventHandler {
            Input.getNoteName().let {
                if (it.first != null && it.first!!.isNotBlank() && it.second != null && it.second!!.isNotBlank())
                    FileTree.addNote(it.first!!, it.second!!)
                rebuildTree()
            }
        }
        buttonAddCode.onMouseClicked = EventHandler {
            Input.getCodeName().let {
                if (it.first != null && it.first!!.isNotBlank() && it.second != null && it.second!!.isNotBlank() && it.third.isNotBlank())
                    FileTree.addFile(it.first!!, it.second!!, it.third)
                rebuildTree()
            }
        }
        buttonOpenExternal.onMouseClicked =
            EventHandler { (treeView.selectionModel.selectedItem as? File)?.openExternally() }
        buttonSave.onMouseClicked = EventHandler {
            if (CodeTab.currentFile != null) {
                CodeTab.currentFile!!.save(CodeTab.textArea.text)
            }
        }
        buttonRunCode.onMouseClicked = EventHandler {
            if (CodeTab.currentFile != null) {
                CodeTab.currentFile!!.save(CodeTab.textArea.text)
                val builder = ProcessBuilder("java", (treeView.selectionModel.selectedItem as File).name)
                builder.directory(Path((treeView.selectionModel.selectedItem as File).jFile.absolutePath).parent.toFile())
                builder.redirectErrorStream(true)
                val proc = builder.start()
                tabPane.selectionModel.select(TerminalTab)
                doWhen({ !proc.isAlive }) {
                    Platform.runLater {
                        TerminalTab.write(
                            IOUtils.toString(
                                proc.inputStream,
                                StandardCharsets.UTF_8
                            )
                        )
                    }
                }
            }
        }
        buttonPdf.onMouseClicked = EventHandler {
            if (treeView.selectionModel.selectedItem != null && treeView.selectionModel.selectedItem is File && FilenameUtils.getExtension(
                    (treeView.selectionModel.selectedItem as File).name
                ) == "tex"
            ) {
                CodeTab.currentFile!!.save(CodeTab.textArea.text)
                println("Compiling latex document")
                val builder =
                    ProcessBuilder("pdflatex", (treeView.selectionModel.selectedItem as File).name)
                builder.directory(Path((treeView.selectionModel.selectedItem as File).jFile.absolutePath).parent.toFile())
                builder.redirectErrorStream(true)
                val proc = builder.start()
                doWhen({ !proc.isAlive }, {
                    kotlin.run {
                        Platform.runLater {
                            TerminalTab.write(
                                IOUtils.toString(
                                    proc.inputStream,
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
                        doWhen(
                            { !xdgProc.isAlive },
                            {
                                Platform.runLater {
                                    TerminalTab.append(
                                        IOUtils.toString(
                                            xdgProc.inputStream,
                                            StandardCharsets.UTF_8
                                        )
                                    )
                                }
                            })
                    }
                })
            }
        }
        buttonReloadFiletree.onMouseClicked = EventHandler { FileTree.refreshTree() }
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                if (CodeTab.currentFile != null) {
                    CodeTab.currentFile!!.save(CodeTab.textArea.text)
                }
            }
        })
        Thread.setDefaultUncaughtExceptionHandler { _, e ->
            if (CodeTab.currentFile != null) {
                CodeTab.currentFile!!.save(CodeTab.textArea.text)
                CodeTab.currentFile = null
            }
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

    fun resize(width: Int, height: Int) {
        hbox.prefWidth = width.toDouble()
        hbox.prefHeight = height.toDouble()
        toolBar.prefHeight = height.toDouble()
        treeView.prefWidth = 30 percentOf width
        treeView.prefHeight = height.toDouble()
        tabPane.prefWidth = 70 percentOf width
        tabPane.prefHeight = width.toDouble()
    }

    private infix fun Int.percentOf(percentage: Int) = this / 100.0 * percentage
}