package uni.notes

import org.apache.commons.io.FilenameUtils
import uni.notes.tabs.FiletypeTab
import uni.notes.tabs.ImageTab
import uni.notes.tabs.WebTab
import uni.notes.types.File

object FiletypeAssocs {

    private val typeMap: HashMap<Array<String>, Class<out FiletypeTab>> = HashMap(
        mutableMapOf(
            Pair(arrayOf("png", "jpg", "jpeg", "gif"), ImageTab::class.java),
            Pair(arrayOf("html"), WebTab::class.java)
        )
    )

    fun hasSpecialTab(file: File) =
        arrayOf("png", "jpg", "jpeg", "gif", "html").contains(FilenameUtils.getExtension(file.name))

    operator fun get(type: String): Class<out FiletypeTab>? {
        for (entry in typeMap) if (entry.key.contains(type)) return entry.value
        return null
    }

}