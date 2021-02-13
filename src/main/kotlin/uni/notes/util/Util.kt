package uni.notes.util

import javafx.scene.control.TreeView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uni.notes.types.File
import uni.notes.ui.Controller
import java.awt.Color

fun Color.asPaint(): javafx.scene.paint.Color = javafx.scene.paint.Color.rgb(red, green, blue, 1.0)

fun <E> ArrayList<E>.addIf(condition: () -> Boolean, item: E) {
    if (condition.invoke()) add(item)
}

@Suppress("unused")
fun Any.discard() {
    return
}

fun doWhen(condition: () -> Boolean, toDo: () -> Unit) {
    GlobalScope.launch {
        run {
            while (!condition.invoke()) delay(10)
            toDo.invoke()
        }
    }
}

infix fun Int.percentOf(percentage: Int) = this / 100.0 * percentage

fun TreeView<String>.getSelectedFile(): File? {
    return when (Controller.cTreeView.selectionModel.selectedItem) {
        null, !is File -> null
        else -> selectionModel.selectedItem!! as File
    }
}

fun Triple<*, *, *>.all(condition: (Any?) -> Boolean): Boolean {
    return this.toList().all { any -> condition.invoke(any) }
}

fun Pair<*, *>.all(condition: (Any?) -> Boolean): Boolean {
    return this.toList().all { any -> condition.invoke(any) }
}

fun String.makeValidFilename(): String {
    return replace(Regex("([/\\\\?*:\"<>,;= ()&#])+"), "-")
}

@Suppress("SpellCheckingInspection")
fun String.makeValidFoldername(): String {
    return replace(Regex("([/\\\\?*:\"<>.,;= ()&#])+"), "-")
}