package uni.notes

import org.apache.commons.io.FileUtils
import uni.notes.types.Note
import uni.notes.types.Subject
import java.io.File
import java.net.URL

object FileTree {

    val subjects: ArrayList<Subject> = ArrayList()
    private var collapseMap = ArrayList<String>()

    private fun listNotesInSubject(subject: File) =
        subject.listFiles { f -> f.isDirectory }

    private fun listFilesInNote(note: File) = note.listFiles()!!.map { file -> uni.notes.types.File(file.name, file) }

    private fun listSubjects() = File(UserPrefs.NOTEBOOK_PATH).listFiles { f -> f.isDirectory }

    private fun buildCollapseTree() {
        subjects.forEach { sub ->
            kotlin.run {
                collapseMap.addIf({ sub.isExpanded }, sub.name)
                sub.notes.forEach { note ->
                    kotlin.run {
                        collapseMap.addIf({ note.isExpanded }, note.name)
                        note.files.forEach { file ->
                            kotlin.run {
                                collapseMap.addIf({ file.isExpanded }, file.name)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun applyCollapseTree() {
        subjects.forEach { sub ->
            sub.isExpanded = collapseMap.contains(sub.name)
            sub.notes.forEach { note ->
                kotlin.run {
                    note.isExpanded = collapseMap.contains(note.name)
                    note.files.forEach { file ->
                        file.isExpanded = collapseMap.contains(file.name)
                    }
                }
            }
        }
    }

    private fun buildTree() {
        subjects.clear()
        listSubjects()!!.forEach { subjectFile ->
            subjects.add(
                Subject(
                    subjectFile.name,
                    ArrayList(listNotesInSubject(subjectFile)!!.map<File, Note> { noteFile ->
                        Note(
                            noteFile.name,
                            ArrayList(listFilesInNote(noteFile).toList())
                        )
                    }.toList())
                )
            )
        }
    }

    fun refreshTree() {
        buildCollapseTree()
        buildTree()
        applyCollapseTree()
    }

    init {
        buildTree()
        buildCollapseTree()
    }

    fun addSubject(name: String) {
        subjects.add(Subject(name, ArrayList()))
        File(UserPrefs.NOTEBOOK_PATH + "/" + name).mkdirs()
        refreshTree()
    }

    @Suppress("NAME_SHADOWING")
    fun addNote(subject: String, note: String) {
        subjectByName(subject).let { sub ->
            FileUtils.copyURLToFile(
                URL(UserPrefs.TEMPLATE_URL),
                File(UserPrefs.NOTEBOOK_PATH + "/" + subject + "/" + note + "/main.tex"),
                10000,
                10000
            )
            sub.notes.add(
                Note(
                    note,
                    ArrayList(
                        listOf(
                            uni.notes.types.File(
                                "main.tex",
                                File(UserPrefs.NOTEBOOK_PATH + "/" + subject + "/" + note + "/main.tex")
                            )
                        )
                    )
                )
            )
            File(UserPrefs.NOTEBOOK_PATH + "/" + subject + "/" + note).mkdirs()
        }
        refreshTree()
    }

    fun addFile(subject: String, note: String, file: String) {
        noteByName(note, subject).files.add(
            uni.notes.types.File(
                file,
                File(UserPrefs.NOTEBOOK_PATH + "/" + subject + "/" + note + "/" + file)
            )
        )
        File(UserPrefs.NOTEBOOK_PATH + "/" + subject + "/" + note + "/" + file).createNewFile()
        refreshTree()
    }

    fun toString(file: uni.notes.types.File) = file.name
    fun toString(note: Note) = note.name
    fun toString(subject: Subject) = subject.name

    fun subjectByName(name: String) = subjects.first { subject -> subject.name == name }
    fun noteByName(note: String, subject: String) = subjectByName(subject).notes.first { n -> n.name == note }
}
