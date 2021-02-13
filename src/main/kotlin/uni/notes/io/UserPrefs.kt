package uni.notes.io

import com.eclipsesource.json.Json
import com.eclipsesource.json.JsonObject
import com.eclipsesource.json.WriterConfig
import java.io.File
import java.io.FileReader

object UserPrefs {

    val ROOT_PATH = System.getProperty("user.home") + File.separator + ".uni-notes"
    val SETTINGS_PATH = ROOT_PATH + File.separator + "settings.ini"
    lateinit var NOTEBOOK_PATH: String
    lateinit var TEMPLATE_URL: String

    private fun loadPrefs() {
        with(File(ROOT_PATH)) {
            if (!exists())
                mkdirs()
        }
        with(File(SETTINGS_PATH)) {
            if (!exists()) {
                writeText(
                    JsonObject().add("notebookPath", ROOT_PATH + File.separator + "notes").add(
                        "templatePath",
                        "https://gist.githubusercontent.com/NastyGamer/722a29264a7d3bad7b0157097b9ec1b2/raw/df1753fcc8710ff5e40427345631cdc2f77e0df4/LaTeX-Template.tex"
                    )
                        .toString(WriterConfig.PRETTY_PRINT)
                )
            }
        }
        NOTEBOOK_PATH = Json.parse(FileReader(File(SETTINGS_PATH))).asObject()["notebookPath"].asString()
        TEMPLATE_URL = Json.parse(FileReader(File(SETTINGS_PATH))).asObject()["templatePath"].asString()
        with(File(NOTEBOOK_PATH)) {
            if (!exists()) mkdirs()
        }
    }

    fun savePrefs() {
        with(File(SETTINGS_PATH)) {
            writeText(
                JsonObject().add("notebookPath", NOTEBOOK_PATH).add("templatePath", TEMPLATE_URL)
                    .toString(WriterConfig.PRETTY_PRINT)
            )
        }
    }

    init {
        loadPrefs()
    }


}