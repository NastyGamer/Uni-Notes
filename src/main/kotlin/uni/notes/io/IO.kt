package uni.notes.io

import javafx.application.Platform
import org.apache.commons.io.IOUtils
import uni.notes.tabs.CodeTab
import uni.notes.tabs.TerminalTab
import uni.notes.ui.Controller
import uni.notes.ui.Icons
import uni.notes.ui.Notifications
import uni.notes.util.doWhen
import uni.notes.util.getSelectedFile
import java.nio.charset.StandardCharsets
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path

object IO {

    fun saveCurrentFile() {
        CodeTab.currentFile?.let {
            if (it.jFile.canWrite())
                it.save(CodeTab.textArea.text)
            else Notifications.showWarning("Unable to write to file ${it.name}")
        }
    }

    fun openCurrentFileExternally() {
        (Controller.cTreeView.getSelectedFile())?.openExternally()
    }

    @Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
    @OptIn(ExperimentalPathApi::class)
    fun buildAndShowPDF() {
        if (!Controller.cButtonPdf.isSelected) return
        Controller.cTreeView.getSelectedFile()?.let {
            Notifications.showInfo("Building ${it.name}", Icons.hammerIcon())
            val pdfLatexBuilder = ProcessBuilder("pdflatex", "-interaction=nonstopmode", "-halt-on-error", it.name)
            pdfLatexBuilder.directory(Path(it.jFile.absolutePath).parent.toFile())
            val pdfLatexProc = pdfLatexBuilder.start()
            doWhen({ !pdfLatexProc.isAlive }) {
                Platform.runLater { TerminalTab.write(IOUtils.toString(pdfLatexProc.inputStream, StandardCharsets.UTF_8)) }
                Platform.runLater { TerminalTab.append(IOUtils.toString(pdfLatexProc.errorStream, StandardCharsets.UTF_8)) }
                if (pdfLatexProc.exitValue() == 0) {
                    val xdgBuilder = ProcessBuilder("xdg-open", it.name.replace("tex", "pdf"))
                    xdgBuilder.directory(Path(it.jFile.absolutePath).parent.toFile())
                    xdgBuilder.redirectErrorStream(true)
                    val xdgProc = xdgBuilder.start()
                    doWhen({ !xdgProc.isAlive })
                    { Platform.runLater { TerminalTab.append(IOUtils.toString(xdgProc.inputStream, StandardCharsets.UTF_8)) } }
                } else
                    Notifications.showWarning("Error compiling")
            }
        }
    }
}