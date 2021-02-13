package uni.notes.buttonHandler

import com.jfoenix.controls.JFXButton
import javafx.application.Platform
import javafx.event.EventHandler
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOUtils
import uni.notes.io.IO
import uni.notes.tabs.TerminalTab
import uni.notes.ui.Controller
import uni.notes.ui.Icons
import uni.notes.ui.Notifications
import uni.notes.util.*
import java.nio.charset.StandardCharsets
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path

@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
@OptIn(ExperimentalPathApi::class)
object RunButtonHandler : ButtonHandler<JFXButton> {

    override fun setupButton(t: JFXButton) {
        t.onMouseClicked = EventHandler {
            IO.saveCurrentFile()
            Controller.cTreeView.getSelectedFile()?.let {
                Notifications.showInfo("Building ${it.name}", Icons.hammerIcon())
                if (FilenameUtils.isExtension(it.name, "java")) {
                    val javaBuilder = ProcessBuilder("java", it.name)
                    javaBuilder.directory(Path(it.jFile.absolutePath).parent.toFile())
                    javaBuilder.redirectErrorStream(true)
                    val javaProc = javaBuilder.start()
                    Controller.cTabPane.selectionModel.select(TerminalTab)
                    doWhen({ !javaProc.isAlive }) {
                        Platform.runLater { TerminalTab.write(IOUtils.toString(javaProc.inputStream, StandardCharsets.UTF_8)) }
                    }
                } else Notifications.showError("File is not a Java source-file")
            }
        }
    }


}