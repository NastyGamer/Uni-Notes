package uni.notes.util

import uni.notes.io.IO
import uni.notes.io.UserPrefs
import uni.notes.tabs.CodeTab
import uni.notes.ui.Notifications
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class CrashHandler : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(t: Thread, e: Throwable) {
        System.err.println(e.stackTraceToString())
        IO.saveCurrentFile()
        CodeTab.currentFile = null
        Notifications.showError("IDE-error occurred")
        java.io.File(UserPrefs.ROOT_PATH + "/" + "crash-${SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Timestamp(Date().time))}")
            .writeText(e.stackTraceToString())
    }
}