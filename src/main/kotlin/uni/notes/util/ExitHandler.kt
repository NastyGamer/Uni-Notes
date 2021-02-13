package uni.notes.util

import uni.notes.io.IO
import uni.notes.io.UserPrefs

class ExitHandler : Thread() {

    override fun run() {
        IO.saveCurrentFile()
        UserPrefs.savePrefs()
    }

}