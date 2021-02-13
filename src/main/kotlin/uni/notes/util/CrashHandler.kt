package uni.notes.util

import uni.notes.io.IO
import uni.notes.io.UserPrefs
import uni.notes.tabs.CodeTab
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import javax.swing.JOptionPane

class CrashHandler : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(t: Thread, e: Throwable) {
        IO.saveCurrentFile()
        CodeTab.currentFile = null
        kotlin.run { JOptionPane.showMessageDialog(null, e.stackTraceToString(), "An error occurred", JOptionPane.ERROR_MESSAGE) }
        java.io.File(UserPrefs.ROOT_PATH + "/" + "crash-${SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Timestamp(Date().time))}")
            .writeText(e.stackTraceToString())
    }
}